package idolondemand;

/**
 * @author Daniyar Itegulov
 */
public enum EntityType {
    PEOPLE_ENG("people_eng"), PLACES_ENG("places_eng"), COMPAINES_ENG("companies_eng");

    private String value;

    EntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
