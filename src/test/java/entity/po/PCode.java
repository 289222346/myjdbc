package entity.po;


import java.math.BigInteger;

public class PCode {

  private BigInteger codeId;
  private BigInteger shortId;
  private String codeType;
  private String value;
  private String codeNane;


  public BigInteger getCodeId() {
    return codeId;
  }

  public void setCodeId(BigInteger codeId) {
    this.codeId = codeId;
  }


  public BigInteger getShortId() {
    return shortId;
  }

  public void setShortId(BigInteger shortId) {
    this.shortId = shortId;
  }


  public String getCodeType() {
    return codeType;
  }

  public void setCodeType(String codeType) {
    this.codeType = codeType;
  }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public String getCodeNane() {
    return codeNane;
  }

  public void setCodeNane(String codeNane) {
    this.codeNane = codeNane;
  }

  @Override
  public String toString() {
    return "PCode{" +
            "codeId=" + codeId +
            ", shortId=" + shortId +
            ", codeType='" + codeType + '\'' +
            ", value='" + value + '\'' +
            ", codeNane='" + codeNane + '\'' +
            '}';
  }
}
