package com.wqb.commons;


import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

//import net.chnbs.delta.framework.json.XML;

/**
 * xml辅助类，提供map对象转换成xml字符串，xml字符串转换成map对象方法
 * @Description	
 * @author ql
 * @date 2014-7-17 下午1:34:17
 * @version V1.0
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XmlMapUtil {
	
	public static void main(String args[]) {
		String xml = 
				"<RESPONSE_NSRMBHDZ class=\"RESPONSE_NSRMBHDZ\" size=\"2\">"
					+ "<RESPONSE_NSRMBMX>"
						+ "<NSRSBH><NSRSBH>320481596926305</NSRSBH></NSRSBH>"
						+ "<NSRSBH><NSRSBH>三三四四</NSRSBH></NSRSBH>"
						+ "<NSRDZDAH/>"
		
						+ "<RESPONSE_NSRMBHD class=\"RESPONSE_NSRMBHD\" size=\"1\">"
							+ "<RESPONSE_NSRMBHDMX>"
								+ "<HY_DM>0203</HY_DM>"
								+ "<HY_MC>机动�?/HY_MC>"
							+ "</RESPONSE_NSRMBHDMX>"
						+ "</RESPONSE_NSRMBHD>"
					+ "</RESPONSE_NSRMBMX>"
						
					+ "<RESPONSE_NSRMBMX>"
						+ "<NSRSBH>320481596926306</NSRSBH>"
						+ "<NSRDZDAH/>"
//						+ "<HY_DM><HY_MC>0203</HY_MC><HY_MC>020d</HY_MC></HY_DM>"
//						+ "<HY_DM>0204</HY_DM>"
						+ "<RESPONSE_NSRMBHD class=\"RESPONSE_NSRMBHD\" size=\"1\">"
							+ "<RESPONSE_NSRMBHDMX>"
								+ "<HY_DM>0204</HY_DM>"
								+ "<HY_MC>二手�?/HY_MC>"
							+ "</RESPONSE_NSRMBHDMX>"
						+ "</RESPONSE_NSRMBHD>"
					+ "</RESPONSE_NSRMBMX>"

					+ "</RESPONSE_NSRMBHDZ>";

		Map map;
		try {
			map = xml2Map(xml);
			System.out.println(map);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static Map xml2Map(Document doc) throws DocumentException{
/*		
  		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
*/
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();

		if (root.attributeValue("size") != null) {
			List rootList = new ArrayList();
			
			Map<String, Object> rootMap = Dom2Map(root);
			
			if (rootMap.size() == 1) {
				for (Map.Entry<String, Object> entry : rootMap.entrySet()) {
//					System.out.println(entry.getKey() + ": " + entry.getValue());
					Object obj = entry.getValue();
					if(!obj.getClass().getName().equals("java.util.ArrayList")){
						rootList.add(entry.getValue());
					}else{
						rootList = (List)entry.getValue();
					}
				}
			}
			map.put(root.getName(), rootList);
		}else{
			map = Dom2Map(root);
		}
		
		return map;
	}
	/**
	 * 将原始xml字符串转换成map对象�?
	 * 如果节点中包含有size属�?，该节点用List集合进行封装，含有size属�?的节点作为key值存存储该集�?
	 * 如果同一节点下有多个同名节点，以数组接收
	 * @param xml
	 * @return
	 */
	public static Map xml2Map(String xml) throws DocumentException{
		Document doc = null;
		int result = StringUtils.indexOfAny(xml, new String[]{"&"});
		if(result != -1){
		    //含有特殊字符
			xml = xml.replace("&", "&amp;");
		}
		doc = DocumentHelper.parseText(xml);
/*		
  		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
*/
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();

		if (root.attributeValue("size") != null) {
			List rootList = new ArrayList();
			
			Map<String, Object> rootMap = Dom2Map(root);
			
			if (rootMap.size() == 1) {
				for (Map.Entry<String, Object> entry : rootMap.entrySet()) {
//					System.out.println(entry.getKey() + ": " + entry.getValue());
					Object obj = entry.getValue();
					if(!obj.getClass().getName().equals("java.util.ArrayList")){
						rootList.add(entry.getValue());
					}else{
						rootList = (List)entry.getValue();
					}
				}
			}
			map.put(root.getName(), rootList);
		}else{
			map = Dom2Map(root);
		}
		
		return map;
	}

	private static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.attributeValue("size") != null) {
					if (map.containsKey(iter.getName())) {
						((List) map.get(iter.getName())).add(Dom2Map(iter));
					} else {
						Map<String, Map> itemMap = Dom2Map(iter);
						
						if (itemMap.size() == 1) {
							Map tmp = new HashMap();
							for (Map.Entry<String, Map> entry : itemMap.entrySet()) {
//								System.out.println(entry.getKey() + ": " + entry.getValue());
//								tmp.put(iter.getName(), entry.getValue());
								Object ob = entry.getValue();
//								String ob_key = entry.getKey();
								String ob_class = ob.getClass().getName();
								
								if (ob.getClass().getName().equals("java.util.ArrayList")) {
//									tmp.put(iter.getName(), entry.getValue());
									map.put(iter.getName(), entry.getValue());
								}else if(ob_class.equals("java.util.HashMap")){
									tmp.putAll(entry.getValue());
									itemMap = tmp;
									List nlist = new ArrayList();
									nlist.add(itemMap);
									map.put(iter.getName(), nlist);
								}else if(ob_class.equals("java.lang.String")){
									
									map.put(iter.getName(), new ArrayList());	//如果该数组为空，则添加空数组实现
								}else{
									

								}
							}
						}
					}
				} else if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}

	/**
	 * Map对象toXml字符�?
	 * @param _obj		map对象
	 * @param rootName	xml根节�?
	 * @return
	 */
	public static String map2XML(Map _obj, String rootName) {
		// log.info("---MapToXmlString--map-->"+_obj);
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append("<" + rootName + ">");
		xml.append(toXML(_obj));
		xml.append("</" + rootName + ">");
		// log.info("--返回客户�?---xml-->"+xml.toString());
		return xml.toString();
	}
	
	public static String map2XML(Map _obj, String rootName, String attr, String attValue) {
		// log.info("---MapToXmlString--map-->"+_obj);
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append("<" + rootName + " " + attr + "=\"" + attValue + "\">");
		xml.append(toXML(_obj));
		xml.append("</" + rootName + ">");
		// log.info("--返回客户�?---xml-->"+xml.toString());
		return xml.toString();
	}
	
	
	public static String map2JSON(Map _obj) {
		Gson g = new Gson();
		String jsonString = g.toJson(_obj);
		return jsonString;
	}
	
	/**
	 * Map对象toXml字符�?
	 * @param _obj
	 * @param
	 * @return
	 */
	public static String map2XML(Map _obj) {
		// log.info("---MapToXmlString--map-->"+_obj);
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append(toXML(_obj));
		// log.info("--返回客户�?---xml-->"+xml.toString());
		return xml.toString();
	}
	
	public static String toXML(Map _obj) {
		StringBuffer sb = new StringBuffer();
		if (_obj != null) {
			Set keySet = _obj.keySet();
			for (Iterator it = keySet.iterator(); it.hasNext();) {
				Object key = it.next();
				Object value = _obj.get(key);
				if(value instanceof Map){
					sb.append(toXMLMap((Map)value,key));
				}
				else if(value instanceof Collection){
					sb.append(toXMLCollection((Collection)value,key));
				}
				//else if(value instanceof String){
				else{
					sb.append(toXMLOtherObject(value,key));
				}
			}
		}
		return sb.toString();
	}

	private  static String toXMLCollection(Collection _list,Object key) {
		StringBuffer sb = new StringBuffer();
		if (_list != null) {
			sb.append("<").append(key).append(" size='"+_list.size()+"'>").append("\n");
//			sb.append("<").append(key).append("LIST>").append("\n");
			for (Iterator it = _list.iterator(); it.hasNext();) {
				sb.append("<").append(key).append(">").append("\n");
				sb.append(toXML((Map)it.next()));
				sb.append("</").append(key).append(">").append("\n");
			}
			sb.append("</").append(key).append(">").append("\n ");
//			sb.append("</").append(key).append("LIST>").append("\n ");
		}
		return sb.toString();
	}

	private static String toXMLMap(Map _map,Object node) {
		StringBuffer sb = new StringBuffer();
		if (_map != null) {
			sb.append("<").append(node).append(">").append("\n");
			sb.append(toXML(_map));
			sb.append("</").append(node).append(">").append("\n");
		}
		return sb.toString();
	}

	private static String toXMLOtherObject(Object _obj, Object key) {
		StringBuffer sb = new StringBuffer();
		if (_obj != null) {
			sb.append("<").append(key).append(">");
//			sb.append(XML.escape(_obj.toString()));
			sb.append(_obj.toString());
			sb.append("</").append(key).append("> ").append("\n ");
		}
		return sb.toString();
	}

	public static  String parseRes(String res){
		String bodyJson ="";
		try{
			Document doc = DocumentHelper.parseText(res);
			Element ebody = doc.getRootElement().element("BODY");
			Element RESULTCODE = doc.getRootElement().element("HEAD").element("RESULT").element("RESULTCODE");
			Element RESULTMSG = doc.getRootElement().element("HEAD").element("RESULT").element("RESULTMSG");
			String message = RESULTMSG.getText();
			String rcode = RESULTCODE.getText();
			if("0000".equals(rcode)){
				bodyJson = Base64Util.getFromBase64(ebody.getText());
			}else{
				Map<String, String> respMap = new HashMap<String, String>();
				respMap.put("RESULT", rcode);
				respMap.put("MSG", message);
				return JSONObject.fromObject(respMap).toString();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return bodyJson;
	}
}
