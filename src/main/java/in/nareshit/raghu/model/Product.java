package in.nareshit.raghu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;

@Data
@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

	@XmlTransient
	private Integer prodId;
	
	@XmlElement(name = "pname")
	private String prodName;
	
	@XmlElement(name = "pcost")
	private Double prodCost;
	
	@XmlElement(name = "pdisc")
	private Double prodDisc;
	
	@XmlElement(name = "pgst")
	private Double prodGst;
}
