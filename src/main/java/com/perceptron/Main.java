package com.perceptron;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    final static double[] array1 = new double[]{1, 0, 0};
    final static double[] array2 = new double[]{0, 1, 0};
    final static double[] array3 = new double[]{0, 0, 1};
    final static String fileName = "C:\\Users\\uhovs\\Desktop\\Tech.xlsx";

    public static void main(String[] args) {
        List<Neuron> layer1 = new ArrayList<>();
        List<Neuron> layer2 = new ArrayList<>();
        List<Neuron> layer3 = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            layer1.add(new Neuron());
        }

        for (int i = 0; i < 27; i++) {
            layer2.add(new Neuron());
        }

        for (int i = 0; i < 3; i++) {
            layer3.add(new Neuron());
        }

        double value;

        for (Neuron n : layer1) {
            for (Neuron n2 : layer2) {
                value = Math.random();
                n.getExit().put(n2, value);
                n2.getEnter().put(n, value);
            }
        }

        for (Neuron n2 : layer2) {
            for (Neuron n3 : layer3) {
                value = Math.random();
                n2.getExit().put(n3, value);
                n3.getEnter().put(n2, value);
            }
        }


        File file = new File(fileName);
        XSSFWorkbook wb = readFile(file);
        if(wb!=null) {
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < 50; i++) {                      //обучение
                startTraining(sheet, layer1, layer2, layer3);
            }
            test(sheet, layer1, layer2, layer3);                 //тест
        }

    }

    @SafeVarargs
    public static void startTraining(XSSFSheet sheet, List<Neuron>... arrayLists) {
        for(Row row : sheet) {
            if(!row.getCell(0).getStringCellValue().equals("Name")) {
                for (int i = 0; i < arrayLists[0].size() - 1; i++) {
                    arrayLists[0].get(i).setValue(row.getCell(i + 2).getNumericCellValue());
                }
                for (int i = 1; i < arrayLists.length; i++) {
                    for (Neuron n : arrayLists[i]) {
                        n.activation();
                    }
                }
                String str = row.getCell(1).getStringCellValue();
                switch (str){
                    case "гражданский":
                        training(array1, arrayLists);
                        break;
                    case "грузовой":
                        training(array2, arrayLists);
                        break;
                    case "военный":
                        training(array3, arrayLists);
                        break;
                }
            }
        }
    }

    @SafeVarargs
    public static void training(double[] array, List<Neuron>... arrayLists) {
        double delta;
        double weight;
        for (int i = 0; i < array.length; i++) {
            Neuron n = arrayLists[arrayLists.length - 1].get(i);
            n.setSigma(array[i] - n.getValue());
        }
        for (int i = arrayLists.length - 2; i >= 0; i--) {
            for (Neuron n : arrayLists[i]) {
                for (Neuron nOut : n.getExit().keySet()) {
                    weight = n.getExit().get(nOut);
                    delta = n.getValue() * nOut.getSigma() * nOut.getValue() * (1 - nOut.getValue());
                    n.getExit().put(nOut, weight + delta);
                    n.setSigma(n.getSigma() + nOut.getSigma() * n.getExit().get(nOut));
                }
            }
        }
        for(List<Neuron> neurons : arrayLists){
            for(Neuron neuron : neurons){
                neuron.setValue(0);
                neuron.setSigma(0);
            }
        }
    }

    public static XSSFWorkbook readFile(File file){
        try {
            return new XSSFWorkbook(file);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static int result(List<Neuron> neurons){
        double value = neurons.get(0).getValue();
        int res = 0;
        for(int i=1;i<neurons.size();i++){
            if(neurons.get(i).getValue()>value) {
                res = i;
                value = neurons.get(i).getValue();
            }
        }
        return res;
    }

    @SafeVarargs
    public static void test(XSSFSheet sheet, List<Neuron>... lists){
        Row row = sheet.getRow(7);
        System.out.println(row.getCell(0).getStringCellValue());
        for(int i=0; i<lists[0].size(); i++){
            lists[0].get(i).setValue(row.getCell(i+2).getNumericCellValue());
        }
        for (int i = 1; i < lists.length; i++) {
            for (Neuron n : lists[i]) {
                n.activation();
            }
        }

        for(Neuron n : lists[lists.length-1]){
            System.out.println(n.getValue());
        }
        int i = result(lists[lists.length-1]);
        switch (i){
            case 0:
                System.out.println("Это гражданский самолет");
                break;
            case 1:
                System.out.println("Это грузовой самолет");
                break;
            case 2:
                System.out.println("Это военный самолет");
                break;
        }
    }
}
