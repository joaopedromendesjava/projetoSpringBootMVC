package com.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component //para o spring framework gerenciar a classe
public class ReportUtil implements Serializable{

	private static final long serialVersionUID = 1L;

	//retorna o pdf em byte para download no navegador
	public byte[] geraRelatorio(List listDados,String relatorio, ServletContext context)  //parametros que ira receber
	
		throws Exception{
		
		//recebe a lista de dados com os atributos e cria lista de dados para o relatorio
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listDados); 
		
		//carrega o caminho do arquivo jasper compilado .jasper
		String caminhoJasper = context.getRealPath("relatorios") + File.separator + relatorio + ".jasper";
	
		//carrega o arquivo jasper passando os dados //caminho e fonte de dados
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(),jrBeanCollectionDataSource);
	
		//exporta para byte para fazer download em pdf
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
	
	
}
