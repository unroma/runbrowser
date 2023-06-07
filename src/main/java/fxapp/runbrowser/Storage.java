package fxapp.runbrowser;

import fxapp.runbrowser.enums.SavedDefaults;
import fxapp.runbrowser.model.TabValue;
import fxapp.runbrowser.utils.EncryptUtils;
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

    public void setToStorage() {
        tabs.clear();
        Storage.getCreatedPanes().values().forEach(controllerPane -> {
            SavedDefaults savedDefault = SavedDefaults.getDefaultByName(controllerPane.getSavedDefault().getValue());
            String urlValue = controllerPane.getUrl().getText();
            if (savedDefault != null && savedDefault != SavedDefaults.EMPTY) {
                urlValue = savedDefault.getUrl();
            }
            TabValue tab = TabValue.builder()
                    .url(urlValue)
                    .savedDefault(savedDefault)
                    .username(EncryptUtils.encryptText(controllerPane.getUsername().getText()))
                    .password(EncryptUtils.encryptText(controllerPane.getPassword().getText()))
                    .build();
            tabs.add(tab);
        });
    }

}
