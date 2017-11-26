package progweb3.poa.ifrs.edu.conectarrest;

import java.io.Serializable;
import java.util.Random;

public class Pessoa implements Serializable {
    int id;
    String nome;
    String cidade;
    String descricao;

    public Pessoa(String nome, String cidade, String descricao) {
        this(0, nome, cidade, descricao);
    }
    public Pessoa(int id, String nome, String cidade, String descricao) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cidade='" + cidade + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }

    private static String[] nomes = {"Marcos", "Arthur", "José", "Carlos", "Karina", "Matheus"};
    private static String[] cidades = {"Rio", "POA", "Canoas", "Viamão", "Sapucaia", "Alvorada"};
    private static String[] descricoes = {"Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam", "Lorem ipsum",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Nullam feugiat, turpis at pulvinar vulputate, erat",
            "Morbi a metus. Phasellus enim erat, vestibulum vel,", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi gravida libero nec velit. Morbi scelerisque luctus velit. Etiam dui sem,"};

    public static Pessoa carrega() {
        return new Pessoa(nomes[getRandomValue(0, 5)],
                cidades[getRandomValue(0, 5)],  descricoes[getRandomValue(0, 5)]);
    }

    private static int getRandomValue(int low, int high) {
        return new Random().nextInt(high - low) + low;
    }
   /* public enum Status {
        OK, INSERIR, ATUALIZAR, EXCLUIR
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pessoa pessoa = (Pessoa) o;

        return id == pessoa.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
