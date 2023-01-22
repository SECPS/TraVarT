package at.jku.cps.travart.core.common;

/**
 * This represents a configurable item of an variablilty model. An item
 * implementing this interface can be selected.
 *
 * @author Johann Stoebich
 * @author Kevin Feichtinger
 */
public interface IConfigurable {

    /**
     * Returns whether the feature is selected or not.
     *
     * @return whether the feature is selected or not.
     */
    boolean isSelected();

    /**
     * De-/Selects the feature.
     *
     * @param selected De-/Selects the configurable.
     */
    void setSelected(boolean selected);

    /**
     * Returns the name of an IConfigurable of a variability model
     *
     * @return the name of the identifiable.
     */
    String getName();
}
