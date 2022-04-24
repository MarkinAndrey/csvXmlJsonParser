import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "staff.csv";
        String[] employeeData = "1,John,Smith,USA,25".split(",");
        String[] employeeData2 = "2,Inav,Petrov,RU,23".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employeeData);
            writer.writeNext(employeeData2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Employee> list = parseCSV(columnMapping, fileName);

        list.forEach(System.out::println);//test

        String json = listToJson(list);

        String fileNameJSON = "data.json";

        writeString(json, fileNameJSON);

        String fileNameXML = "data.xml";

        List<Employee> list2 = parseXML(fileNameXML);
        list2.forEach(System.out::println);//test

        String fileNameXMLtoJSON = "data1.json";

        writeString(listToJson(list2), fileNameXMLtoJSON);

    }

     public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .build();
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName))
                    .withCSVParser(parser)
                    .build();
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(strategy)
                    .build();// тут я не понял почему он ругается то, как проверить непроверенное

            list = csvToBean.parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    static <T> String listToJson(List<T> list){
        Type listType = new TypeToken<List<T>>() {}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        return gson.toJson(list, listType);
    }

    static void writeString(String data, String fileName){
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<Employee> parseXML(String fileName)
            throws ParserConfigurationException, IOException, SAXException {

        List<Employee> employees = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        Node node = doc.getDocumentElement();
        NodeList nodeLists = node.getChildNodes();

        for (int i = 0; i < nodeLists.getLength(); i++) {
            if (Node.ELEMENT_NODE != nodeLists.item(i).getNodeType()) {
                continue;
            }

            NodeList nodeListEmploueers = nodeLists.item(i).getChildNodes();
            long id = 0;
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;

            for (int j = 0; j < nodeListEmploueers.getLength(); j++) {
                if (Node.ELEMENT_NODE != nodeListEmploueers.item(j).getNodeType()){
                    continue;
                }
                String textContent = nodeListEmploueers.item(j).getTextContent();
                switch (nodeListEmploueers.item(j).getNodeName()) {
                    case "id" :
                        //noinspection UnnecessaryBoxing
                        id = Long.valueOf(textContent);
                        break;
                    case "firstName" :
                        firstName = textContent;
                        break;
                    case "lastName" :
                        lastName = textContent;
                        break;
                    case "country" :
                        country = textContent;
                        break;
                    case "age" :
                        //noinspection UnnecessaryBoxing
                        age = Integer.valueOf(textContent);
                        break;
                    default:
                }
            }
            Employee employee = new Employee(id, firstName, lastName, country, age);
            employees.add(employee);
        }
        return employees;
    }

}
