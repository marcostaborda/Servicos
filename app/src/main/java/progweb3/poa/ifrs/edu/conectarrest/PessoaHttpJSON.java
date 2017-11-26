package progweb3.poa.ifrs.edu.conectarrest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PessoaHttpJSON {
    public static final String PESSOAS_URL_JSON = "https://raw.githubusercontent.com/sbertagnolli/progweb3-json/master/pessoas.json";
    private static HttpURLConnection connectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection)url.openConnection();
        conexao.setReadTimeout(10 * SEGUNDOS);
        conexao.setConnectTimeout(15 * SEGUNDOS);
        conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        conexao.setDoOutput(false);
        conexao.connect();
        return conexao;
    }
    public static boolean isOnLine(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    public static List<Pessoa> carregarPessoasJson() {
        try {
            HttpURLConnection conexao = connectar(PESSOAS_URL_JSON);
            int resposta = conexao.getResponseCode();
            if (resposta ==  HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonPessoas(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Pessoa> lerJsonPessoas(JSONObject json) throws JSONException {
        List<Pessoa> lista = new ArrayList<>();
        JSONArray jsonPessoas = json.getJSONArray("pessoas");
        for (int i = 0; i < jsonPessoas.length(); i++) {
                JSONObject jsonPessoa = jsonPessoas.getJSONObject(i);
                Pessoa pessoa = new Pessoa(
                        jsonPessoa.getString("nome"),
                        jsonPessoa.getString("cidade"),
                        jsonPessoa.getString("descricao"));
                lista.add(pessoa);
        }
        return lista;
    }
    private static String bytesParaString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        // O bufferzao vai armazenar todos os bytes lidos
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        // precisamos saber quantos bytes foram lidos
        int bytesLidos;
        // Vamos lendo de 1KB por vez...
        while ((bytesLidos = is.read(buffer)) != -1) {
            // copiando a quantidade de bytes lidos do buffer para o bufferzão
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }


    }

