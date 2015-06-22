package guo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**=========================================================

  ������

=========================================================**/
public class ConnectMySQL
{
  public static void main(String[] args) throws Exception
  {
    DumpTables oDump = new DumpTables();
    oDump.Initialize();
    oDump.Dump();
  }
}

/**=========================================================

  �����ʾ

=========================================================**/
class DumpTables
{
  private Connection oConnection = null;
  private Statement oStatement = null;
  
  public void Initialize()
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL����
      oConnection = DriverManager.getConnection(
        "jdbc:mysql://127.0.0.1:3306/guodb", "root", "");
      oStatement = oConnection.createStatement();
    }
    catch(Exception e)
    {
      System.out.println("MySQL error:" + e.getMessage());
    }
  }
  
  public void Dump()
  {
    DumpAddressBook();
    DumpTestBin();
    
/*
    try
    {
      oStatement.executeUpdate(
        "update addressbook set cellPhone = 'GoodMorning' where name <=> 'Good'; ");
    }
    catch(Exception e)
    {
      System.out.println("MySQL error:" + e.getMessage());
    }
    
    try
    {
      oStatement.executeUpdate(
        "insert into addressbook values('13', 'Test2', 'Hello')");
    }
    catch(Exception e)
    {
      System.out.println("MySQL error:" + e.getMessage());
    }
    
    DumpAddressBook();
*/
  }
  
  private void DumpAddressBook()
  {
    try
    {
      System.out.println("============================");
      System.out.println("==== Table: addressbook ====");
      System.out.println("============================");
      //��ѯ���ݲ����
      ResultSet selectRes = oStatement.executeQuery(
        "SELECT name, cellPhone FROM addressbook");
      while(selectRes.next())
      {
        //ѭ����������
        String name = selectRes.getString("name");
        String cellPhone = selectRes.getString("cellPhone");
        System.out.println("name: " + name + "\tcellPhone: " + cellPhone);
      }
    }
    catch(Exception e)
    {
      System.out.println("MySQL error:" + e.getMessage());
    }
  }
  
  private void DumpTestBin()
  {
    try
    {
      System.out.println("============================");
      System.out.println("====== Table: testbin ======");
      System.out.println("============================");
      ResultSet selectRes = oStatement.executeQuery(
        "SELECT HEX(id) as id FROM testbin");
      while (selectRes.next()) 
      {
        //ѭ����������
        String id = selectRes.getString("id");
        System.out.println("id: " + id);
      }
    }
    catch(Exception e)
    {
      System.out.println("MySQL error:" + e.getMessage());
    }
  }
}
