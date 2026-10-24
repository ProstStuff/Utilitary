package dev.proststuff.utilitary.config.serialization.format.ops;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import de.marhali.json5.*;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Json5Ops implements DynamicOps<Json5Element> {
    public static Json5Null NULL = new Json5Null();
    public static Json5Ops INSTANCE = new Json5Ops(false);
    public static Json5Ops COMPRESSED = new Json5Ops(true);

    private final boolean compressed;

    protected Json5Ops(boolean compressed) {
        this.compressed = compressed;
    }

    @Override
    public Json5Element empty() {
        return NULL;
    }

    @Override
    public Json5Element emptyMap() {
        return new Json5Object();
    }

    @Override
    public Json5Element emptyList() {
        return new Json5Array();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, Json5Element input) {
        if (input instanceof Json5Object) {
            return convertMap(outOps, input);
        }
        if (input instanceof Json5Array) {
            return convertList(outOps, input);
        }
        if (input instanceof Json5Null) {
            return outOps.empty();
        }
        
        Json5Primitive primitive = input.getAsJson5Primitive();
        
        if (primitive.isString()) {
            return outOps.createString(primitive.getAsString());
        }
        if (primitive.isBoolean()) {
            return outOps.createBoolean(primitive.getAsBoolean());
        }
        BigDecimal value = primitive.getAsBigDecimal();
        try {
            long l = value.longValueExact();
            if ((byte) l == l) {
                return outOps.createByte((byte) l);
            }
            if ((short) l == l) {
                return outOps.createShort((short) l);
            }
            if ((int) l == l) {
                return outOps.createInt((int) l);
            }
            return outOps.createLong(l);
        } catch (ArithmeticException e) {
            double d = value.doubleValue();
            if ((float) d == d) {
                return outOps.createFloat((float) d);
            }
            return outOps.createDouble(d);
        }
    }

    @Override
    public DataResult<Number> getNumberValue(Json5Element input) {
        if (input instanceof Json5Primitive) {
            if (input.getAsJson5Primitive().isNumber()) {
                return DataResult.success(input.getAsNumber());
            }
            if (compressed && input.getAsJson5Primitive().isString()) {
                try {
                    return DataResult.success(Integer.parseInt(input.getAsString()));
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> "Not a number: " + e + " " + input);
                }
            }
        }
        return DataResult.error(() -> "Not a number: " + input);
    }

    @Override
    public Json5Element createNumeric(Number i) {
        return Json5Primitive.fromNumber(i);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(Json5Element input) {
        if (input instanceof Json5Primitive && input.getAsJson5Primitive().isBoolean()) {
            return DataResult.success(input.getAsBoolean());
        }
        return DataResult.error(() -> "Not a boolean: " + input);
    }

    @Override
    public Json5Element createBoolean(boolean value) {
        return Json5Primitive.fromBoolean(value);
    }

    @Override
    public DataResult<String> getStringValue(Json5Element input) {
        if (input instanceof Json5Primitive) {
            if (input.getAsJson5Primitive().isString() || input.getAsJson5Primitive().isNumber() && compressed) {
                return DataResult.success(input.getAsString());
            }
        }
        return DataResult.error(() -> "Not a string: " + input);
    }

    @Override
    public Json5Element createString(String value) {
        return Json5Primitive.fromString(value);
    }

    @Override
    public DataResult<Json5Element> mergeToList(Json5Element list, Json5Element value) {
        if (!(list instanceof Json5Array) && list != empty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        }

        Json5Array result = new Json5Array();
        if (list != empty()) {
            result.addAll(list.getAsJson5Array());
        }
        result.add(value);
        return DataResult.success(result);
    }

    @Override
    public DataResult<Json5Element> mergeToList(Json5Element list, List<Json5Element> values) {
        if (!(list instanceof Json5Array) && list != empty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        }

        if (values.isEmpty()) {
            if (list == empty()) {
                return DataResult.success(emptyList());
            }
            return DataResult.success(list);
        }

        Json5Array result = new Json5Array();
        if (list != empty()) {
            result.addAll(list.getAsJson5Array());
        }
        values.forEach(result::add);
        return DataResult.success(result);
    }

    @Override
    public DataResult<Json5Element> mergeToMap(Json5Element map, Json5Element key, Json5Element value) {
        if (!(map instanceof Json5Object) && map != empty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }
        if (!(key instanceof Json5Primitive) || !key.getAsJson5Primitive().isString() && !compressed) {
            return DataResult.error(() -> "key is not a string: " + key, map);
        }

        Json5Object output = new Json5Object();
        if (map != empty()) {
            map.getAsJson5Object().entrySet().forEach(entry -> output.add(entry.getKey(), entry.getValue()));
        }
        output.add(key.getAsString(), value);

        return DataResult.success(output);
    }

    @Override
    public DataResult<Json5Element> mergeToMap(Json5Element map, MapLike<Json5Element> values) {
        if (!(map instanceof Json5Object) && map != empty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }

        Iterator<Pair<Json5Element, Json5Element>> valuesIterator = values.entries().iterator();
        if (!valuesIterator.hasNext()) {
            if (map == empty()) {
                return DataResult.success(emptyMap());
            }
            return DataResult.success(map);
        }
        Json5Object output = new Json5Object();
        if (map != empty()) {
            map.getAsJson5Object().entrySet().forEach(entry -> output.add(entry.getKey(), entry.getValue()));
        }

        List<Json5Element> missed = Lists.newArrayList();

        valuesIterator.forEachRemaining(entry -> {
            Json5Element key = entry.getFirst();
            if (!(key instanceof Json5Primitive) || !key.getAsJson5Primitive().isString() && !compressed) {
                missed.add(key);
                return;
            }
            output.add(key.getAsString(), entry.getSecond());
        });

        if (!missed.isEmpty()) {
            return DataResult.error(() -> "some keys are not strings: " + missed, output);
        }

        return DataResult.success(output);
    }

    @Override
    public DataResult<Stream<Pair<Json5Element, Json5Element>>> getMapValues(Json5Element input) {
        if (!(input instanceof Json5Object)) {
            return DataResult.error(() -> "Not a JSON object: " + input);
        }
        return DataResult.success(input.getAsJson5Object().entrySet().stream().map(entry -> Pair.of(Json5Primitive.fromString(entry.getKey()), entry.getValue() instanceof Json5Null ? null : entry.getValue())));
    }

    @Override
    public DataResult<Consumer<BiConsumer<Json5Element, Json5Element>>> getMapEntries(Json5Element input) {
        if (!(input instanceof Json5Object)) {
            return DataResult.error(() -> "Not a JSON object: " + input);
        }
        return DataResult.success(c -> {
            for (Map.Entry<String, Json5Element> entry : input.getAsJson5Object().entrySet()) {
                c.accept(createString(entry.getKey()), entry.getValue() instanceof Json5Null ? null : entry.getValue());
            }
        });
    }

    @Override
    public DataResult<MapLike<Json5Element>> getMap(Json5Element input) {
        if (!(input instanceof Json5Object)) {
            return DataResult.error(() -> "Not a JSON object: " + input);
        }
        Json5Object object = input.getAsJson5Object();
        return DataResult.success(new MapLike<Json5Element>() {
            @Nullable
            @Override
            public Json5Element get(Json5Element key) {
                Json5Element element = object.get(key.getAsString());
                if (element instanceof Json5Null) {
                    return null;
                }
                return element;
            }

            @Nullable
            @Override
            public Json5Element get(String key) {
                Json5Element element = object.get(key);
                if (element instanceof Json5Null) {
                    return null;
                }
                return element;
            }

            @Override
            public Stream<Pair<Json5Element, Json5Element>> entries() {
                return object.entrySet().stream().map(e -> Pair.of(Json5Primitive.fromString(e.getKey()), e.getValue()));
            }

            @Override
            public String toString() {
                return "MapLike[" + object + "]";
            }
        });
    }

    @Override
    public Json5Element createMap(Stream<Pair<Json5Element, Json5Element>> map) {
        Json5Object result = new Json5Object();
        map.forEach(p -> result.add(p.getFirst().getAsString(), p.getSecond()));
        return result;
    }

    @Override
    public DataResult<Stream<Json5Element>> getStream(Json5Element input) {
        if (input instanceof Json5Array) {
            return DataResult.success(StreamSupport.stream(input.getAsJson5Array().spliterator(), false).map(e -> e instanceof Json5Null ? null : e));
        }
        return DataResult.error(() -> "Not a json array: " + input);
    }

    @Override
    public DataResult<Consumer<Consumer<Json5Element>>> getList(Json5Element input) {
        if (input instanceof Json5Array) {
            return DataResult.success(c -> {
                for (Json5Element element : input.getAsJson5Array()) {
                    c.accept(element instanceof Json5Null ? null : element);
                }
            });
        }
        return DataResult.error(() -> "Not a json array: " + input);
    }

    @Override
    public Json5Element createList(Stream<Json5Element> input) {
        Json5Array result = new Json5Array();
        input.forEach(result::add);
        return result;
    }

    @Override
    public Json5Element remove(Json5Element input, String key) {
        if (input instanceof Json5Object) {
            Json5Object result = new Json5Object();
            input.getAsJson5Object().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), key)).forEach(entry -> result.add(entry.getKey(), entry.getValue()));
            return result;
        }

        return input;
    }

    @Override
    public String toString() {
        return "JSON";
    }

    @Override
    public ListBuilder<Json5Element> listBuilder() {
        return new Json5Ops.ArrayBuilder();
    }

    private static class ArrayBuilder implements ListBuilder<Json5Element> {
        private DataResult<Json5Array> builder = DataResult.success(new Json5Array(), Lifecycle.stable());

        @Override
        public DynamicOps<Json5Element> ops() {
            return INSTANCE;
        }

        @Override
        public ListBuilder<Json5Element> add(Json5Element value) {
            builder = builder.map(b -> {
                b.add(value);
                return b;
            });
            return this;
        }

        @Override
        public ListBuilder<Json5Element> add(DataResult<Json5Element> value) {
            builder = builder.apply2stable((b, element) -> {
                b.add(element);
                return b;
            }, value);
            return this;
        }

        @Override
        public ListBuilder<Json5Element> withErrorsFrom(DataResult<?> result) {
            builder = builder.flatMap(r -> result.map(v -> r));
            return this;
        }

        @Override
        public ListBuilder<Json5Element> mapError(UnaryOperator<String> onError) {
            builder = builder.mapError(onError);
            return this;
        }

        @Override
        public DataResult<Json5Element> build(Json5Element prefix) {
            DataResult<Json5Element> result = builder.flatMap(b -> {
                if (!(prefix instanceof Json5Array) && prefix != ops().empty()) {
                    return DataResult.error(() -> "Cannot append a list to not a list: " + prefix, prefix);
                }

                Json5Array array = new Json5Array();
                if (prefix != ops().empty()) {
                    array.addAll(prefix.getAsJson5Array());
                }
                array.addAll(b);
                return DataResult.success(array, Lifecycle.stable());
            });

            builder = DataResult.success(new Json5Array(), Lifecycle.stable());
            return result;
        }
    }

    @Override
    public boolean compressMaps() {
        return compressed;
    }

    @Override
    public RecordBuilder<Json5Element> mapBuilder() {
        return new Json5Ops.JsonRecordBuilder();
    }

    private class JsonRecordBuilder extends RecordBuilder.AbstractStringBuilder<Json5Element, Json5Object> {
        protected JsonRecordBuilder() {
            super(Json5Ops.this);
        }

        @Override
        protected Json5Object initBuilder() {
            return new Json5Object();
        }

        @Override
        protected Json5Object append(String key, Json5Element value, Json5Object builder) {
            builder.add(key, value);
            return builder;
        }

        @Override
        protected DataResult<Json5Element> build(Json5Object builder, Json5Element prefix) {
            if (prefix == null || prefix instanceof Json5Null) {
                return DataResult.success(builder);
            }

            if (prefix instanceof Json5Object) {
                Json5Object result = new Json5Object();
                for (Map.Entry<String, Json5Element> entry : prefix.getAsJson5Object().entrySet()) {
                    result.add(entry.getKey(), entry.getValue());
                }
                for (Map.Entry<String, Json5Element> entry : builder.entrySet()) {
                    result.add(entry.getKey(), entry.getValue());
                }
                return DataResult.success(result);
            }

            return DataResult.error(() -> "mergeToMap called with not a map: " + prefix, prefix);
        }
    }
}
