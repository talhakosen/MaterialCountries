package kosen.talha.viagogo.com.viagogo.pojos;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kosen.talha.viagogo.com.viagogo.R;

/**
 * Created by talhacevher on 06/03/2015.
 */

// why parcable ? http://prolificinteractive.com/blog/2014/07/18/why-we-love-parcelable
public class CountryModal implements Parcelable {
    private String name;
    private String localizedName;
    private String capital;
    private String relevance;
    private String region;
    private String subregion;
    private String demonym;
    private String alpha2Code;
    private String alpha3Code;
    private String nativeName;

    private String altSpellings;
    private String latlng;
    private String timezones;
    private String translations;
    private String borders;
    private String callingCodes;
    private String topLevelDomain;
    private String currencies;
    private String languages;

    private Integer population;
    private Integer area;
    private Double gini;

    public CountryModal(Context context, JSONObject country) throws JSONException {
        this.name = getJsonString(country, "name");
        this.capital = getJsonString(country, "capital");
        this.relevance = getJsonString(country, "relevance");
        this.region = getJsonString(country, "region");
        this.subregion = getJsonString(country, "subregion");
        this.demonym = getJsonString(country, "demonym");
        this.alpha2Code = getJsonString(country, "alpha2Code");
        this.alpha3Code = getJsonString(country, "alpha3Code");
        this.nativeName = getJsonString(country, "nativeName");
        this.altSpellings = getJsonString(country, "altSpellings");
        this.latlng = getJsonString(country, "latlng");
        this.timezones = getJsonString(country, "timezones");
        this.translations = getJsonString(country, "translations");
        this.borders = getJsonString(country, "borders");
        this.callingCodes = getJsonString(country, "callingCodes");
        this.topLevelDomain = getJsonString(country, "topLevelDomain");
        this.currencies = getJsonString(country, "currencies");
        this.languages = getJsonString(country, "languages");

        this.population = getJsonInt(country, "population");
        this.area = getJsonInt(country, "area");
        this.gini = getJsonDouble(country, "gini");

        if(!context.getResources().getString(R.string.lang).equals("en")){
            JSONObject jsonTranslations = new JSONObject(translations);
            this.localizedName = jsonTranslations.getString(context.getResources().getString(R.string.lang));
        }
        else
            this.localizedName= this.name;
    }

    public CountryModal(Parcel in) {
        this.name = in.readString();
        this.capital = in.readString();
        this.relevance = in.readString();
        this.region = in.readString();
        this.subregion = in.readString();
        this.demonym = in.readString();
        this.alpha2Code = in.readString();
        this.alpha3Code = in.readString();
        this.nativeName = in.readString();
        this.altSpellings = in.readString();
        this.latlng = in.readString();
        this.timezones = in.readString();
        this.translations = in.readString();
        this.borders = in.readString();
        this.callingCodes = in.readString();
        this.topLevelDomain = in.readString();
        this.currencies = in.readString();
        this.languages = in.readString();
        this.localizedName = in.readString();

        this.population = in.readInt();
        this.area = in.readInt();
        this.gini = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(capital);
        dest.writeString(relevance);
        dest.writeString(region);
        dest.writeString(subregion);
        dest.writeString(demonym);
        dest.writeString(alpha2Code);
        dest.writeString(alpha3Code);
        dest.writeString(nativeName);

        dest.writeString(altSpellings);
        dest.writeString(latlng);
        dest.writeString(timezones);
        dest.writeString(translations);
        dest.writeString(borders);
        dest.writeString(callingCodes);
        dest.writeString(topLevelDomain);
        dest.writeString(currencies);
        dest.writeString(languages);
        dest.writeString(localizedName);

        dest.writeInt(population);
        dest.writeInt(area);
        dest.writeDouble(gini);

    }

    public static final Parcelable.Creator<CountryModal> CREATOR = new Parcelable.Creator<CountryModal>() {
        public CountryModal createFromParcel(final Parcel in) {
            return new CountryModal(in);
        }

        public CountryModal[] newArray(int size) {
            return new CountryModal[size];
        }
    };

    public String getJsonString(JSONObject country, String key) {
        try {
            return country.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    public int getJsonInt(JSONObject country, String key) {
        try {
            return country.getInt(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    public Double getJsonDouble(JSONObject country, String key) {
        try {
            return country.getDouble(key);
        } catch (JSONException e) {
            return 0.0;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getDemonym() {
        return demonym;
    }

    public void setDemonym(String demonym) {
        this.demonym = demonym;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getAltSpellings() {
        return altSpellings;
    }

    public void setAltSpellings(String altSpellings) {
        this.altSpellings = altSpellings;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getTimezones() {
        return timezones;
    }

    public void setTimezones(String timezones) {
        this.timezones = timezones;
    }

    public String getBorders() {
        return borders;
    }

    public void setBorders(String borders) {
        this.borders = borders;
    }

    public String getCallingCodes() {
        return callingCodes;
    }

    public void setCallingCodes(String callingCodes) {
        this.callingCodes = callingCodes;
    }

    public String getTopLevelDomain() {
        return topLevelDomain;
    }

    public void setTopLevelDomain(String topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
    }

    public String getCurrencies() {
        return currencies;
    }

    public void setCurrencies(String currencies) {
        this.currencies = currencies;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Double getGini() {
        return gini;
    }

    public void setGini(Double gini) {
        this.gini = gini;
    }

    public String getTranslations() {
        return translations;
    }

    public void setTranslations(String translations) {
        this.translations = translations;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }
}
