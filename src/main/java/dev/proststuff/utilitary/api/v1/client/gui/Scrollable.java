package dev.proststuff.utilitary.api.v1.client.gui;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public interface Scrollable {
    ScrollDirection getScrollDirection();
    boolean canScroll(ScrollDirection scrollDirection);
    int getScrollableHeight();
    int getScrollableWidth();

    interface Element extends Scrollable {
        int getOriginX();
        int getOriginY();

        void setX(int x);
        void setY(int y);
        int getWidth();
        int getHeight();

        @Override
        default int getScrollableHeight() {
            return getHeight();
        }

        @Override
        default int getScrollableWidth() {
            return getWidth();
        }

        default void applyScroll(Scrollable.Container container) {
            ScrollDirection containerScrollDirection  = container.getScrollDirection();
            ScrollDirection elementScrollDirection = getScrollDirection();

            boolean scrollVertically = containerScrollDirection.canScrollVertically() && elementScrollDirection.canScrollVertically();
            boolean scrollHorizontally = containerScrollDirection.canScrollHorizontally() && elementScrollDirection.canScrollHorizontally();

            if (scrollVertically) setY((int) (getOriginY() + container.getScrollY()));
            if (scrollHorizontally) setX((int) (getOriginX() + container.getScrollX()));
        }
    }
    
    interface Container extends Scrollable {
        double getScrollY();
        double getScrollX();

        default boolean canScroll() {
            return canScroll(getScrollDirection());
        }

        default void applyScroll() {
            getScrollableElements().forEach((element) -> element.applyScroll(this));
        }

        List<? extends GuiEventListener> children();

        default List<Element> getScrollableElements() {
            List<Element> list = new ArrayList<>();
            
            for (GuiEventListener child : children()) {
                if (child instanceof Element element) {
                    list.add(element);
                }
            }
            
            return list;
        }

        default int getContentHeight() {
            int height = 0;

            for (Element element : getScrollableElements()) {
                height += element.getScrollableHeight();
            }

            return height;
        }

        default int getContentWidth() {
            int width = 0;

            for (Element element : getScrollableElements()) {
                width += element.getScrollableWidth();
            }

            return width;
        }

        default double evaluateScrollY(double scrollY) {
            int maxScroll = Math.max(0, getContentHeight() - getScrollableHeight());
            return Mth.clamp(scrollY, 0.0, maxScroll);
        }

        default double evaluateScrollX(double scrollX) {
            int maxScroll = Math.max(0, getContentWidth() - getScrollableWidth());
            return Mth.clamp(scrollX, 0.0, maxScroll);
        }
    }
    
    enum ScrollDirection {
        VERTICAL(true, false),
        HORIZONTAL(false, true),
        MULTI(true, true),
        LOCKED(false, false);

        private final boolean vertical;
        private final boolean horizontal;

        ScrollDirection(boolean vertical, boolean horizontal) {
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

        public boolean canScrollVertically() {
            return vertical;
        }

        public boolean canScrollHorizontally() {
            return horizontal;
        }

        public boolean canScrollWith(boolean vertical, boolean horizontal) {
            return (vertical == this.vertical && this.vertical) || (horizontal == this.horizontal && this.horizontal);
        }

        public boolean canScrollWith(ScrollDirection direction) {
            return canScrollWith(direction.vertical, direction.horizontal);
        }
    }
}
