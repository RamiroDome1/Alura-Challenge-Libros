package Alura_Challenge.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiConsumer(List<LibroDto> results) {


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LibroDto(String title,
                           List<String> languages,
                           int download_count,
                           List<Autores> authors){


        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Autores (String name,
                               Integer birth_year,
                               Integer death_year
                               ){

        }
    }
}
