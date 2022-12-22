package de.tcompart.library.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequest {

  @NotNull(message = "No authors specified. Specify at least one.")
  @NotEmpty(message = "Invalid list of authors. Specify at least one.")
  private List<String> authors;

  @NotBlank(message = "Invalid title. It is not allowed to be empty.")
  private String title;

  @NotNull(message = "Invalid publication date. It is not allowed to be null. Please specify in the pattern of dd-MM-yyyy.")
  @JsonFormat
      (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDate published;

}
