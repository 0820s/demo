package com.example.demo.controller;

import com.example.demo.entities.Prediction;
import com.example.demo.entities.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class IndexController {
    @RequestMapping(value="/index")
    public String index(Model model, HttpSession session){
        String path=System.getProperty("path");
        String filePath;
        if(path==null){
            filePath="D:\\java\\predict\\samples_neg";
        }else{
            filePath=path+"/samples_neg";
        }
        File files=new File(filePath);
        ArrayList<String> fileName=new ArrayList<>();

        if(files.exists()){
            File[] array=files.listFiles();
            for(File f:array){
                if(f.isFile()){
                    String name=f.getName();
                    if(name.endsWith("csv")){
                        fileName.add(name);
                    }
                }
            }
        }

        Prediction prediction;
        Date date;
        String file;
        Server server;
        String state="healthy";
        if(session.getAttribute("prediction")==null){
            prediction=new Prediction(0,0,0,0,0,0,0);
            date=new Date(System.currentTimeMillis());
            file="No files here";
            server=new Server("","","","","");
        }else{
            prediction=(Prediction) session.getAttribute("prediction");
            date=(Date) session.getAttribute("date");
            file=session.getAttribute("file").toString();
            server=(Server) session.getAttribute("server");
            state=session.getAttribute("state").toString();
        }
        model.addAttribute("fileName",fileName);
        model.addAttribute("prediction",prediction);
        model.addAttribute("date",date);
        model.addAttribute("file",file);
        model.addAttribute("server",server);
        model.addAttribute("state",state);
        return "index";
    }

    @PostMapping(value = "/result")
    public String result(@RequestParam("file") String file, HttpSession session){
        String path=System.getProperty("path");
        String filePath;
        String pythonPath;
        if(path==null){
            pythonPath="D:\\java\\predict\\model\\xgbst_demo.py ";
            filePath="D:\\java\\predict\\samples_neg\\"+file;
            path="D:\\java\\predict";
        }else{
            pythonPath=path+"/model/xgbst_demo.py ";
            filePath=path+"/samples_neg/"+file;
        }
        ArrayList<Float> label=new ArrayList<>();
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        String serverBrand="unkown";
        String platform="unkown";
        String memoryManufactory="unkown";
        String memorySpeed="unkown";
        String CPUSpeed="unkown";
        String state="healthy";
        try{
            String command="python "+pythonPath+" "+filePath+" "+path;
            Process proc=Runtime.getRuntime().exec(command);
            proc.waitFor();
            BufferedReader in=new BufferedReader(new InputStreamReader(proc.getInputStream()));
            date=simpleDateFormat.parse(in.readLine());
            serverBrand=in.readLine();
            platform=in.readLine();
            memoryManufactory=in.readLine();
            memorySpeed=in.readLine();
            CPUSpeed=in.readLine();
            for(int i=0;i<7;i++){
                Float prob=Float.parseFloat(in.readLine());
                if(prob>0.5){
                    state="fail";
                }
                label.add(prob);
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("D:\\java\\data\\"+file));
//            reader.readLine();
//            String line = null;
//            while((line=reader.readLine())!=null){
//                String item[] = line.split(",");
//                float value = Float.parseFloat(item[item.length-1]);
//                label.add(value);
//
//                date=simpleDateFormat.parse(item[0]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        int size= label.size();
        Prediction prediction;
        if(size>=7){
            prediction=new Prediction(label.get(size-1),label.get(size-2),label.get(size-3),label.get(size-4),label.get(size-5),label.get(size-6),label.get(size-7));
        }else{
            prediction=new Prediction(0,0,0,0,0,0,0);
        }
        Server server=new Server(serverBrand,platform,CPUSpeed,memoryManufactory,memorySpeed);
        session.setAttribute("prediction",prediction);
        session.setAttribute("server",server);
        session.setAttribute("date",date);
        session.setAttribute("file",file);
        session.setAttribute("state",state);
        return "redirect:/index.html";
    }
}
