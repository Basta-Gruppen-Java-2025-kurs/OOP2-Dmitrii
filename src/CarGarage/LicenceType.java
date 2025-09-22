package CarGarage;

import Helpers.Named;

public enum LicenceType implements Named {
    A, B, C, D;

    @Override
    public String getName() {
        return this.toString();
    }
}
