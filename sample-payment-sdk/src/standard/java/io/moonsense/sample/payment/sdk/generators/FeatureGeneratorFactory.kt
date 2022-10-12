package io.moonsense.sample.payment.sdk.generators

import io.moonsense.sdk.features.FeatureGenerator

/**
 * Responsible for returning [FeatureGenerator]s if there are
 * any.
 */
internal object FeatureGeneratorFactory {

    /**
     * The standard version does not record features.
     */
    fun getFeatureGenerators(): List<FeatureGenerator> = emptyList()
}