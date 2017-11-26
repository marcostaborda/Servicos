package progweb3.poa.ifrs.edu.conectarrest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


public class PessoaService {
  /*  public static final String SERVIDOR = "http://10.0.2.2:80/android_service";
    private static final String WEBSERVICE_URL = SERVIDOR +"/webservice.php";*/

    private static final String WEBSERVICE_URL ="http://10.0.2.2:8080/ServicoREST/rest/pessoas";

    public PessoaService() { }

    public List<Pessoa> carregarPessoas() {
        List<Pessoa> listaPessoas = null;
        try {
            HttpURLConnection conexao = abrirConexao(new URL(WEBSERVICE_URL), "GET", false);
            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String jsonString = streamToString(conexao.getInputStream());
                Type collectionType = new TypeToken<List<Pessoa>>(){}.getType();
                listaPessoas = (List<Pessoa>) new Gson().fromJson( jsonString , collectionType);
                /*for (Pessoa p: listaPessoas) {
                    Log.d("Pessoa:", p.toString());
                }*/
            }
        } catch (Exception e) {
            Log.e("PessoaService", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return listaPessoas;
    }
    public boolean inserir(Pessoa pessoa){
        boolean sucesso = false;
        try {
            sucesso = enviarPessoa("POST", pessoa);
        } catch (Exception e) {
            Log.e("PessoaService", "Exception: " + e.getMessage());
        }
        return sucesso;
    }
    public boolean excluir(Pessoa pessoa){
        boolean sucesso = false;
        try {
            sucesso = enviarPessoa("DELETE", pessoa);
        } catch (Exception e) {
            Log.e("PessoaService", "Exception: " + e.getMessage());
        }
        return sucesso;
    }
    private HttpURLConnection abrirConexao(URL urlCon, String metodo, boolean doOutput) throws Exception {

        HttpURLConnection conexao = (HttpURLConnection) urlCon.openConnection();
        conexao.setReadTimeout(15000);
        conexao.setConnectTimeout(15000);
        conexao.setRequestMethod(metodo);
        conexao.setDoInput(true);
        conexao.setDoOutput(doOutput);
        if (doOutput) {
            conexao.addRequestProperty("Content-Type", "application/json");
        }
        conexao.connect();
        return conexao;
    }

    private boolean enviarPessoa(String metodoHttp, Pessoa pessoa) throws Exception {
        boolean sucesso = false;
        boolean doOutput = !"DELETE".equals(metodoHttp);
        String url = WEBSERVICE_URL;
        if (!doOutput) {
            System.out.println("url -> "+url);
            url += "/"+ pessoa.id;
        }

        HttpURLConnection conexao = abrirConexao(new URL(url), metodoHttp, doOutput);
        if (doOutput) {
            OutputStream os = conexao.getOutputStream();
            os.write(pessoaToJsonBytes(pessoa));
            os.flush();
            os.close();
        }
        int responseCode = conexao.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream is = conexao.getInputStream();
            String s = streamToString(is);

            is.close();
            JSONObject json = new JSONObject(s);
            pessoa.id = json.getInt("id");
            sucesso = true;
        } else {
            sucesso = false;
        }
        conexao.disconnect();
        return sucesso;
    }

    private String streamToString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }
    private byte[] pessoaToJsonBytes(Pessoa pessoa) {
        try {
            JSONObject jsonPessoa = new JSONObject();
            if(pessoa!=null) Log.e("PS", ""+pessoa.id);
            jsonPessoa.put("id", pessoa.id);
            if(pessoa!=null) Log.e("PS", ""+pessoa.nome);
            jsonPessoa.put("nome", pessoa.nome);
            if(pessoa!=null) Log.e("PS", ""+pessoa.cidade);
            jsonPessoa.put("cidade", pessoa.cidade);
            if(pessoa!=null) Log.e("PS", ""+pessoa.descricao);
            jsonPessoa.put("descricao", pessoa.descricao);
            String json = jsonPessoa.toString();
            Log.e("PSJ", "JSON"+json);
            return json.getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
