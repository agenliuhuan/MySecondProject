package com.xmlcreate;
  
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.ProcessingInstruction;
import org.dom4j.VisitorSupport;
  
/** 
 * 定义自己的访问者类 
 */  
public class MyVisitor extends VisitorSupport {  
   /** 
    * 对于属性节点，打印属性的名字和值 
    */  
   public void visit(Attribute node) {  
       System.out.println("attribute : " + node.getName() + " = "  
              + node.getValue());  
   }  
  
   /** 
    * 对于处理指令节点，打印处理指令目标和数据 
    */  
   public void visit(ProcessingInstruction node) {  
       System.out.println("PI : " + node.getTarget() + " "  
              + node.getText());  
   }  
  
   /** 
    * 对于元素节点 
    * 如果包含文本内容，则打印元素的名字和元素的内容。如果不是，则只打印元素的名字 
    */  
   public void visit(Element node) {  
       if (node.isTextOnly())  
          System.out.println("element : " + node.getName() + " = "  
                 + node.getText());  
       else  
          System.out.println("《《《《《《" + node.getName() + "》》》》》》");  
   }  
}  