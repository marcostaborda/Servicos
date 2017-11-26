package progweb3.poa.ifrs.edu.conectarrest;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MyAdapterCard extends RecyclerView.Adapter<MyAdapterCard.MyViewHolderCard> {
        static Context contexto;
        private final List<Pessoa> listaPessoas;
        private Pessoa pessoa;
        private Activity activity = null;

        public MyAdapterCard(Activity atividade, Context ctx, List<Pessoa> list){
            this.contexto = ctx;
            this.listaPessoas = list;
            this.activity = atividade;
        }

        public MyAdapterCard(List<Pessoa> list){
            this.listaPessoas = list;
        }
        @Override
        public MyViewHolderCard onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_itens_recycler_card, viewGroup, false);
            return new MyViewHolderCard(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolderCard viewHolder, int position) {
            pessoa = listaPessoas.get(position);
            viewHolder.viewNome.setText(pessoa.nome );
            viewHolder.viewCidadeDesc.setText( pessoa.cidade+ "\n" + pessoa.descricao);
            viewHolder.delete.setOnClickListener(viewHolder);
         }

        @Override
        public int getItemCount() {
            return listaPessoas != null ? listaPessoas.size() : 0;

        }

    public interface ExcluirPessoa {
        void excluir(Pessoa pessoa);
    }

        protected class MyViewHolderCard extends RecyclerView.ViewHolder implements View.OnClickListener{
            protected TextView viewNome;
            protected TextView viewCidadeDesc;
            protected ImageButton delete;

            public MyViewHolderCard(View itemView) {
                super(itemView);
                viewNome = (TextView) itemView.findViewById(R.id.card_title);
                viewCidadeDesc = (TextView) itemView.findViewById(R.id.card_desc);
                delete = (ImageButton) itemView.findViewById(R.id.card_delete);
            }

            @Override
            public void onClick(View v) {
                if(v.equals(delete)){
                    removeAt(getAdapterPosition());
                }
            }
            public void removeAt(int position) {
                if(activity!=null) {
                    ExcluirPessoa listener = (ExcluirPessoa) activity;
                    listener.excluir(listaPessoas.get(position));
                }
                listaPessoas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listaPessoas.size());
            }
        }
    }
