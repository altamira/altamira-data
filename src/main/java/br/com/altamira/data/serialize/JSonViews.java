package br.com.altamira.data.serialize;

/**
 *
 * @author alessandro.holanda
 */
public class JSonViews {

    /**
     *
     */
    public static class DefaultView extends JSonViews { }

    /**
     *
     */
    public static class ListView extends DefaultView { }

    /**
     *
     */
    public static class EntityView extends ListView { }
}
