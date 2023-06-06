package fxapp.runbrowser;

import fxapp.runbrowser.model.TabValue;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class Storage {

    @Getter
    private final Map<String, ControllerPane> createdPanes = new LinkedHashMap<>();
    @Getter
    private final List<TabValue> tabs = new ArrayList<>();
    @Getter
    private final List<ControllerPanes> main = new ArrayList<>();
}
