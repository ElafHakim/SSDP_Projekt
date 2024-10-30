package edu.udo.cs.rvs.ssdp;


import java.util.UUID;

/**
 * Die Klasse ServiceIdentifier speichert eine eindeutige UUID und den Service-Typ.
 * Diese Informationen können genutzt werden, um eindeutige Service-IDs zu verwalten
 * und später in einer neuen Liste zurückzugeben.
 */
public class ServiceIdentifier{

     UUID uuid = null;
     String serviceType = null;

    /**public ServiceIdentifier(UUID uuid, String serviceType) {
        this.uuid = uuid;
        this.serviceType = serviceType;
    }*/

  


    public UUID getUuid() {
        return uuid;
    }

    public String getServiceTyp() {
        return serviceType;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void serviceTyp(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "ServiceIdentifier{" +
                "uuid=" + uuid +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}