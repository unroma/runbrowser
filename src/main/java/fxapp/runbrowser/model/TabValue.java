package fxapp.runbrowser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fxapp.runbrowser.enums.SavedDefaults;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
public class TabValue {

    @JsonProperty("url")
    private String url;
    @JsonProperty("savedDefault")
    private SavedDefaults savedDefault;
    @JsonProperty("relative")
    private String relative;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
