package ntnu.idatt2105.config

import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.model.Section
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper
                .configuration
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE).fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
        return modelMapper
    }
}