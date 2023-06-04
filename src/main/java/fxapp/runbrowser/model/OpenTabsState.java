package fxapp.runbrowser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
public class OpenTabsState {

    @JsonProperty("default")
    private Boolean defaultProfile;
    @JsonProperty("loadWhenStart")
    private Boolean loadWhenStart;
    @JsonProperty("path")
    private String path;
    @JsonProperty("tabs")
    private List<TabValue> tabs;
}
