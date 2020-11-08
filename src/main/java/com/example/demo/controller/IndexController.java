package com.example.demo.controller;

import com.example.demo.entities.Prediction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class IndexController {
    @RequestMapping(value="/index")
    public String index(Model model, HttpSession session){
        File files=new File("D:\\java\\data");
        File[] array=files.listFiles();
        ArrayList<String> fileName=new ArrayList<>();

        for(File f:array){
            if(f.isFile()){
                fileName.add(f.getName());
            }
        }
        model.addAttribute("fileName",fileName);

        Prediction prediction;
        Date date;
        String file;
        if(session.getAttribute("prediction")==null){
            prediction=new Prediction(0,0,0,0,0,0,0);
            date=new Date(System.currentTimeMillis());
            file=fileName.get(0);
        }else{
            prediction=(Prediction) session.getAttribute("prediction");
            date=(Date) session.getAttribute("date");
            file=session.getAttribute("file").toString();
        }
        model.addAttribute("prediction",prediction);
        model.addAttribute("date",date);
        model.addAttribute("file",file);
        return "index";
    }

    @PostMapping(value = "/result")
    public String result(@RequestParam("file") String file, HttpSession session){
        ArrayList<Float> label=new ArrayList<>();
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\java\\data\\"+file));
            reader.readLine();
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");
                float value = Float.parseFloat(item[item.length-1]);
                label.add(value);

                date=simpleDateFormat.parse(item[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int size= label.size();
        Prediction prediction;
        if(size>=7){
            prediction=new Prediction(label.get(size-1),label.get(size-2),label.get(size-3),label.get(size-4),label.get(size-5),label.get(size-6),label.get(size-7));
        }else{
            prediction=new Prediction(0,0,0,0,0,0,0);
        }
        session.setAttribute("prediction",prediction);
        session.setAttribute("date",date);
        session.setAttribute("file",file);
        return "redirect:/index.html";
    }
}
