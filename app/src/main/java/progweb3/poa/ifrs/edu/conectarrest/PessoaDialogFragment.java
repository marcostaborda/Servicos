package progweb3.poa.ifrs.edu.conectarrest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class PessoaDialogFragment extends DialogFragment
        implements TextView.OnEditorActionListener {
    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_PESSOA = "pessoa";
    private Activity activity = null;
    private EditText txtNome;
    private EditText txtCidade;
    private EditText txtDescricao;
    private Pessoa pessoa;

    public static PessoaDialogFragment newInstance(Pessoa pessoa) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PESSOA, pessoa);
        PessoaDialogFragment dialog = new PessoaDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pessoa = (Pessoa) getArguments().getSerializable(EXTRA_PESSOA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_dialog_pessoa, container, false);
        txtNome = (EditText) rootView.findViewById(R.id.txtNome);
        txtNome.requestFocus();
        txtCidade = (EditText) rootView.findViewById(R.id.txtEndereco);
        txtCidade.setOnEditorActionListener(this);
        txtDescricao = (EditText) rootView.findViewById(R.id.txtDescricao);
        if(pessoa==null) pessoa = new Pessoa(txtNome.getText().toString(), txtCidade.getText().toString(),txtDescricao.getText().toString());
        if (pessoa != null) {
            txtNome.setText(pessoa.nome);
            txtCidade.setText(pessoa.cidade);
            txtDescricao.setText(pessoa.descricao);
        }
        // Exibe o teclado virtual ao exibir o Dialog
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(R.string.acao_novo);
        return rootView;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (EditorInfo.IME_ACTION_DONE == actionId) {
            activity = getActivity();
            if (activity instanceof SalvarPessoa) {
                if (pessoa == null) {
                    pessoa = new Pessoa(
                            txtNome.getText().toString(),
                            txtCidade.getText().toString(),
                            txtDescricao.getText().toString());
                } else {
                    pessoa.nome = txtNome.getText().toString();
                    pessoa.cidade = txtCidade.getText().toString();
                    pessoa.descricao = txtDescricao.getText().toString();
                }
                SalvarPessoa listener = (SalvarPessoa) activity;
                listener.salvar(pessoa);
                // Feche o dialog
                dismiss();
                return true;
            }
        }
        return false;
    }

    public void abrir(FragmentManager fm) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
        }
    }

    public interface SalvarPessoa {
        void salvar(Pessoa pessoa);
    }
}
