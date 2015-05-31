package idolondemand.entityextractor;

/**
 * @author Daniyar Itegulov
 */
public enum EntityType {
    PEOPLE_ENG("people_eng"), PLACES_ENG("places_eng"), COMPAINES_ENG("companies_eng"), ORGANIZATIONS("organizations"),
    LANUGUAGES("laguages"), UNIVERSITY("universities"), FILMS("films"), ADDRESSES_US("address_us"), PERSONS("person_fullname_eng"),
    NUMBER_PHONES("number_phone_us"), DATE("date_eng"), INTERNET("internet"), BANKACCOUNT("bankaccount_us");

    private String value;

    EntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
