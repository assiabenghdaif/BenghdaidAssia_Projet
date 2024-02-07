package fsm.miaad.models;

import java.util.Date;

public class History {
// La date, l’heure de début et
//de fin de l’activité et le type d’activité
    private  int id;
    private String activity;
    private String dateDe;
    private String dateFi;
    private String email;
    private double LatitudeStart,LongitudeStart,LatitudeFinish,LongitudeFinish;

    public double getLatitudeStart() {
        return LatitudeStart;
    }

    public void setLatitudeStart(double latitudeStart) {
        LatitudeStart = latitudeStart;
    }

    public double getLongitudeStart() {
        return LongitudeStart;
    }

    public void setLongitudeStart(double longitudeStart) {
        LongitudeStart = longitudeStart;
    }

    public double getLatitudeFinish() {
        return LatitudeFinish;
    }

    public void setLatitudeFinish(double latitudeFinish) {
        LatitudeFinish = latitudeFinish;
    }

    public double getLongitudeFinish() {
        return LongitudeFinish;
    }

    public void setLongitudeFinish(double longitudeFinish) {
        LongitudeFinish = longitudeFinish;
    }

    public History(int id, String activity, String dateDe, String dateFi, String email, double latitudeStart, double longitudeStart, double latitudeFinish, double longitudeFinish) {
        this.id = id;
        this.activity = activity;
        this.dateDe = dateDe;
        this.dateFi = dateFi;
        this.email = email;
        LatitudeStart = latitudeStart;
        LongitudeStart = longitudeStart;
        LatitudeFinish = latitudeFinish;
        LongitudeFinish = longitudeFinish;
    }
    
    //activity,dateDe,dateFi,LatitudeStart,LongitudeStart,LatitudeFinish,LongitudeFinish


    public History(String activity, String dateDe, String dateFi, double latitudeStart, double longitudeStart, double latitudeFinish, double longitudeFinish) {
        this.activity = activity;
        this.dateDe = dateDe;
        this.dateFi = dateFi;
        LatitudeStart = latitudeStart;
        LongitudeStart = longitudeStart;
        LatitudeFinish = latitudeFinish;
        LongitudeFinish = longitudeFinish;
    }

    public History(double latitudeStart, double longitudeStart, double latitudeFinish, double longitudeFinish) {
        LatitudeStart = latitudeStart;
        LongitudeStart = longitudeStart;
        LatitudeFinish = latitudeFinish;
        LongitudeFinish = longitudeFinish;
    }

    public History() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDateDe() {
        return dateDe;
    }

    public void setDateDe(String dateDe) {
        this.dateDe = dateDe;
    }

    public String getDateFi() {
        return dateFi;
    }

    public void setDateFi(String  dateFi) {
        this.dateFi = dateFi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
