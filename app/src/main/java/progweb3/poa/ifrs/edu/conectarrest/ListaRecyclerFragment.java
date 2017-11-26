package progweb3.poa.ifrs.edu.conectarrest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListaRecyclerFragment extends InternetFragment {
    private RecyclerView recyclerView;
    TextView mTextMensagem;
    ProgressBar mProgressBar;
    MyAdapterCard adapter;
    private List<Pessoa> listaPessoas = new ArrayList<>();

    private PessoasTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_lista_recycler, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager =  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mTextMensagem = (TextView)rootView.findViewById(android.R.id.empty);
        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);

        /*floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria uma nova pessoa - adiciona a pessoa e avisa o adapter que o conteúdo da lista foi alterado
                listaPessoas.add(Pessoa.carrega());
                adapter.notifyDataSetChanged();

            }
        });*/
        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (listaPessoas == null) {
            listaPessoas = new ArrayList<>();
        }
        adapter = new MyAdapterCard(listaPessoas);
        recyclerView.setAdapter(adapter);
        if (mTask == null) {
            if (PessoaHttpJSON.isOnLine(getActivity())) {
                iniciarDownload();
            } else {
                mTextMensagem.setText("Sem conexão");
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            exibirProgress(true);
        }
    }
    private void exibirProgress(boolean exibir) {
        if (exibir) {
            mTextMensagem.setText("Baixando informações das pessoas - JSON...");
        }
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
    public void iniciarDownload() {
        if (mTask == null ||  mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new PessoasTask();
            mTask.execute();
        }
    }
    class PessoasTask extends AsyncTask<Void, Void, List<Pessoa>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }
        @Override
        protected List<Pessoa> doInBackground(Void... strings) {
            return PessoaHttpJSON.carregarPessoasJson();
        }
        @Override
        protected void onPostExecute(List<Pessoa> pessoas) {
            super.onPostExecute(pessoas);
            exibirProgress(false);
            if (pessoas != null) {
                listaPessoas.clear();
                listaPessoas.addAll(pessoas);
                adapter.notifyDataSetChanged();
            } else {
                mTextMensagem.setText("Falha ao carregar pessoas");
            }
        }
    }
}