package progweb3.poa.ifrs.edu.conectarrest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaRecyclerCard extends AppCompatActivity implements PessoaDialogFragment.SalvarPessoa, MyAdapterCard.ExcluirPessoa{
    private RecyclerView recyclerView;
    MyAdapterCard adapter;
    private List<Pessoa> listaPessoas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager =  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        new PessoasTask("GET", null).execute();
        adapter = new MyAdapterCard(this, ListaRecyclerCard.this, listaPessoas);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void salvar(Pessoa pessoa) {
        listaPessoas.add(pessoa);
        adapter.notifyItemInserted(listaPessoas.size());
        adapter.notifyDataSetChanged();
        new PessoasTask("POST", pessoa).execute();
    }

     public void excluir(Pessoa pessoa){
         Log.d("MAC", pessoa.toString());
         if(pessoa!=null){
             new PessoasTask("DELETE", pessoa).execute();
         }
    }


    public class PessoasTask extends AsyncTask<Void, Void, Void> {
        private String metodo;
        private Pessoa pessoa;


        public PessoasTask(String metodo, Pessoa pessoa) {
            this.metodo = metodo;
            this.pessoa = pessoa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListaRecyclerCard.this,"Carregando dados do serviço", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (metodo.equalsIgnoreCase("GET")) {
                listaPessoas.clear();
                listaPessoas = new PessoaService().carregarPessoas();
            }
            if (metodo.equalsIgnoreCase("POST")) {
                new PessoaService().inserir(pessoa);
            }
            if (metodo.equalsIgnoreCase("DELETE")) {
                new PessoaService().excluir(pessoa);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(listaPessoas == null)
                Toast.makeText(getApplicationContext(),"Banco sem dados cadastrados ", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            adapter = new MyAdapterCard(listaPessoas);
            recyclerView.setAdapter(adapter);
        }
    }
}