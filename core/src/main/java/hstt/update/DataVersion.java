package hstt.update;

import org.json.JSONObject;

import hstt.util.Logger;

public class DataVersion {
  private int status;
  private int versionCode;
  private String downloadurl;
  private String mustUpdate;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(int versionCode) {
    this.versionCode = versionCode;
  }

  public String getDownloadurl() {
    return downloadurl;
  }

  public void setDownloadurl(String downloadurl) {
    this.downloadurl = downloadurl;
  }

  public String getMustUpdate() {
    return mustUpdate;
  }

  public void setMustUpdate(String mustUpdate) {
    this.mustUpdate = mustUpdate;
  }



  public static DataVersion decodeJson(String input)
  {
    Logger.d(input);
    DataVersion dv = new DataVersion();
    dv.setStatus(1);
    try
    {
      JSONObject jsonObjecta = new JSONObject(input);
      int status = jsonObjecta.getInt("status");
      dv.setStatus(status);
      JSONObject jo = jsonObjecta.getJSONObject("data");
      dv.setVersionCode(jo.getInt("versionCode"));
      dv.setDownloadurl(jo.getString("downloadurl"));
      dv.setMustUpdate(jo.getString("MustUpdate"));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return dv;
  }


}
