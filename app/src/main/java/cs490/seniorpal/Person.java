package cs490.seniorpal;

//Person Class for storing data on emergency contacts
public class Person {

    public String firstName="",lastName = "",phoneNumber="",phoneCarrier="",sendTextString="",relation="";
    public Person(String firstName, String lastName, String relation, String phoneString, String phoneCarrier){
        this.firstName = firstName;
        this.lastName = lastName;
        this.relation = relation;
        this.phoneNumber = phoneString;
        this.phoneCarrier=phoneCarrier;
        //Depending on phone carrier automatically appends email SMS suffix to enable SMS texting via email(free)
        switch(phoneCarrier.toUpperCase()){
            case "AT&ampT":
                this.sendTextString=""+phoneString+"@txt.att.net";
                break;
            case "T-MOBILE":
                this.sendTextString=""+phoneString+"@tmomail.net";
                break;
            case "VERIZON":
                this.sendTextString=""+phoneString+"@vtext.com";
                break;
            case "SPRINT":
                this.sendTextString=""+phoneString+"@pm.sprint.com";
                break;
            case "VIRGIN MOBILE":
                this.sendTextString=""+phoneString+"number@vmobl.com";
                break;
            case "TRACFONE":
                this.sendTextString=""+phoneString+"@mmst5.tracfone.com";
                break;
            case "METRO PCS":
                this.sendTextString=""+phoneString+"@mymetropcs.com";
                break;
            case "BOOST MOBILE":
                this.sendTextString=""+phoneString+"@myboostmobile.com";
                break;
            case "CRICKET":
                this.sendTextString=""+phoneString+"@sms.mycricket.com";
                break;
            case "NEXTEL":
                this.sendTextString=""+phoneString+"@messaging.nextel.com";
                break;
            case "ALLTEL":
                this.sendTextString=""+phoneString+"@message.alltel.com";
                break;
            case "PTEL":
                this.sendTextString=""+phoneString+"@ptel.com";
                break;
            case "SUNCOM":
                this.sendTextString=""+phoneString+"@tms.suncom.com";
                break;
            case "QWEST":
                this.sendTextString=""+phoneString+"@qwestmp.com";
                break;
            case "U.S. CELLULAR":
                this.sendTextString=""+phoneString+"@email.uscc.net";
                break;
            default:
                this.sendTextString=""+phoneString+"@txt.att.net";
                break;
        }
    }
    public String getFirstName(){return this.firstName;}
    public String getLastName(){
        return this.lastName;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public String getPhoneCarrier(){
        return phoneCarrier;
    }
    public String getSendTextString(){
        return this.sendTextString;
    }
    public String getRelation(){return this.relation;}
    public String toString(){
        return "Person: "+getFirstName()+" "+getLastName()+ ", "+getPhoneNumber();
    }

}
