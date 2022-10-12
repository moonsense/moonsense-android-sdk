package io.moonsense.sample.payment.sdk.generators

import io.moonsense.features.MeanFeatureGenerator
import io.moonsense.features.StandardDeviationFeatureGenerator
import io.moonsense.sdk.config.SensorType
import io.moonsense.sdk.features.FeatureGenerator

/**
 * Responsible for returning [FeatureGenerator]s if there are
 * any.
 */
internal object FeatureGeneratorFactory {

    /**
     * Return some sample features to record.
     */
    fun getFeatureGenerators() = listOf(
        MeanFeatureGenerator(listOf(SensorType.ACCELEROMETER)),
        StandardDeviationFeatureGenerator(
            listOf(
                SensorType.LINEAR_ACCELEROMETER,
                SensorType.ORIENTATION
            )
        )
    )
}
