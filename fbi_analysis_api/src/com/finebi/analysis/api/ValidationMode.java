
package com.finebi.analysis.api;

/**
 * The validation mode to be used by the provider for the persistence
 * unit.
 * 
 * @since Advanced FineBI Analysis 1.0
 */
public enum ValidationMode {
   
    /**
     * If a Bean Validation provider is present in the environment,
     * the persistence provider must perform the automatic validation
     * of entities.  If no Bean Validation provider is present in the
     * environment, no lifecycle event validation takes place.
     * This is the default behavior.
     */
    AUTO,

    /**
     * The persistence provider must perform the lifecycle event
     * validation.  It is an error if there is no Bean Validation
     * provider present in the environment.
     */
    CALLBACK,

    /**
     * The persistence provider must not perform lifecycle event validation.
     */
    NONE
    }
