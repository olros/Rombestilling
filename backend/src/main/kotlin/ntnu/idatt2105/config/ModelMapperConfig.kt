package ntnu.idatt2105.config

import ntnu.idatt2105.section.dto.SectionChildrenDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.model.Section
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.modelmapper.spi.DestinationSetter
import org.modelmapper.spi.SourceGetter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper
                .configuration
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(false)
                .setMatchingStrategy(MatchingStrategies.LOOSE).fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE

        return modelMapper
    }
}