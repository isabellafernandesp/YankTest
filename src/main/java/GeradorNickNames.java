import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeradorNickNames {

	private List<String> nicknames;
    private List<List<String>> nick_cpf;

    public GeradorNickNames() {
        nicknames = new ArrayList<String>();
        nick_cpf = new ArrayList<List<String>>();
    }
    
    public void getPageNicks(String URL) {
        try {
            Document document = Jsoup.connect(URL).get();
            Elements nicks = document.select("span[class=\"generated-nick\"]");

            for (Element nick : nicks) {
                nicknames.add(nick.attr("text"));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
    }
	
    public void getNicksCpf() throws IOException {
    	List<String> s_nick_cpf = new ArrayList<String>();
    	s_nick_cpf.add("Nick;CPF");
        for (String nick : nicknames) {
        	String URL = "https://www.4devs.com.br/ferramentas_online.php?acao=gerar_cpf&pontuacao=S";
        	Document document = Jsoup.connect(URL).get();
            Elements cpfs = document.select("div[id=\"texto_cpf\"]");
            
            for (Element cpf : cpfs) {
            	s_nick_cpf.add(nick +';'+cpf.attr("text"));
                nick_cpf.add(s_nick_cpf);
            }
        }
    }

    public void writeToFile(String filename) {
        FileWriter writer;
        try {
            writer = new FileWriter(filename);
            nick_cpf.forEach(a -> {
                try {
                    String temp = a.get(0) + ";" + a.get(1) + "\n";
                    writer.write(temp);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
    	GeradorNickNames gerador = new GeradorNickNames();
    	String URL = "https://www.google-analytics.com/collect?v=1&_v=j86&a=1023368811&t=event&_s=2&dl=https%3A%2F%2Fwww.4devs.com.br%2Fgerador_de_nicks&ul=pt-br&de=UTF-8&dt=Gerador%20de%20Nicks%20online%20-%204Devs&sd=24-bit&sr=1366x768&vp=1349x150&je=0&ec=NickGenerator&ea=generate&el=Aleat%C3%B3rio&_u=SACAAEABAAAAAC~&jid=&gjid=&cid=1472450577.1601035539&tid=UA-66505558-21&_gid=743494359.1601035539&gtm=2wg9g1M4TJKV6&z=97642719";
        gerador.getPageNicks(URL);
        gerador.getNicksCpf();
        gerador.writeToFile("Nicks CPF.txt");
    }

}
