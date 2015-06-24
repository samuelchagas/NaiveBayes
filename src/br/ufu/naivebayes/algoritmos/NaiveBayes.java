package br.ufu.naivebayes.algoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufu.naivebayes.modelos.Amostra;
import br.ufu.naivebayes.modelos.Classe;

public class NaiveBayes {

	private int n, d; // n = qntd de amostras; d = qntd atributos

	private Map<Classe, List<Amostra>> amostrasGeral; // todas amostras lidas do arquivo
	private List<Amostra> amostrasTreinamento; // amostras de treinamento
	private List<Amostra> amostrasValidacao; // amostras de validacao

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public Map<Classe, List<Amostra>> getAmostrasGeral() {
		return amostrasGeral;
	}

	public void setAmostrasGeral(Map<Classe, List<Amostra>> amostrasGeral) {
		this.amostrasGeral = amostrasGeral;
	}

	public List<Amostra> getAmostrasTreinamento() {
		return amostrasTreinamento;
	}

	public void setAmostrasTreinamento(List<Amostra> amostrasTreinamento) {
		this.amostrasTreinamento = amostrasTreinamento;
	}

	public List<Amostra> getAmostrasValidacao() {
		return amostrasValidacao;
	}

	public void setAmostrasValidacao(List<Amostra> amostrasValidacao) {
		this.amostrasValidacao = amostrasValidacao;
	}

	public void imprimirAmostras() {
		for (Map.Entry<Classe, List<Amostra>> entry : amostrasGeral.entrySet()) {
			System.out.println("Classe: " + entry.getKey());
			for (Amostra amostra : entry.getValue()) {
				for (int i = 0; i < amostra.getAtributos().length; i++)
					System.out.print(amostra.getAtributos()[i] + " ");
				System.out.println();
			}
			System.out.println();
		}
	}

	public void treinar() {
		Classe classe;

		// inicializa as medias e os desvios padrão por atributo de cada classe com 0
		for (Map.Entry<Classe, List<Amostra>> entry : amostrasGeral.entrySet()) {
			entry.getKey().setMediaAtributo(new double[d]);
			entry.getKey().setDpAtributo(new double[d]);
		}

		// calcula as medias por atributo de todas as amostras de treinamento
		for (int i = 0; i < amostrasTreinamento.size(); i++) {
			classe = getClasseById(amostrasTreinamento.get(i).getClasse());
			for (int j = 0; j < d; j++) {
				classe.getMediaAtributo()[j] += amostrasTreinamento.get(i).getAtributos()[j];
			}
		}
		for (Map.Entry<Classe, List<Amostra>> entry : amostrasGeral.entrySet()) {
			for (int i = 0; i < d; i++) {
				entry.getKey().getMediaAtributo()[i] = (entry.getKey().getMediaAtributo()[i] / entry.getValue().size());
			}
		}

		// calcula os desvios padrao por atributo de todas as amostras de treinamento
		for (int i = 0; i < amostrasTreinamento.size(); i++) {
			classe = getClasseById(amostrasTreinamento.get(i).getClasse());
			for (int j = 0; j < d; j++) {
				classe.getDpAtributo()[j] += Math.pow((amostrasTreinamento.get(i).getAtributos()[j] - classe.getMediaAtributo()[j]), 2);
			}
		}
		for (Map.Entry<Classe, List<Amostra>> entry : amostrasGeral.entrySet()) {
			for (int i = 0; i < d; i++) {
				entry.getKey().getDpAtributo()[i] = Math.sqrt(entry.getKey().getDpAtributo()[i] / (entry.getValue().size() - 1)); // variancia amostral
			}
		}

	}

	public String classificar(Amostra amostra) {
		Classe classe;
		double pctgPorClasse[] = new double[amostrasGeral.size()];

		// algoritmo de naive bayes
		for (int i = 0; i < amostrasGeral.size(); i++) {
			classe = getClasseByIndex(i);
			pctgPorClasse[i] = (amostrasGeral.get(classe).size() / (double) amostrasTreinamento.size());
			for (int j = 0; j < amostra.getAtributos().length; j++) {
				// aplica distribuicao gaussiana
				pctgPorClasse[i] *= (getDistribuicaoGaussiana(amostra.getAtributos()[j], classe.getMediaAtributo()[j], classe.getDpAtributo()[j]));
			}
		}

		// retorn a classe que obteve maior probabilidade
		return getClasseByIndex(getIndexMaxPctg(pctgPorClasse)).getId();
	}

	public Classe getClasseById(String id) {
		for (Classe classe : amostrasGeral.keySet()) {
			if (classe.equals(new Classe(id)))
				return classe;
		}

		return null;
	}

	private double getDistribuicaoGaussiana(double x, double media, double dp) {
		return (1 / Math.sqrt(2 * Math.PI * Math.pow(dp, 2))) * Math.exp(-Math.pow(x - media, 2) / (2 * Math.pow(dp, 2)));
	}

	private Classe getClasseByIndex(int index) {
		return new ArrayList<Classe>(amostrasGeral.keySet()).get(index);
	}

	private int getIndexMaxPctg(double pctgPorClasse[]) {
		double maxPctg = 0.0;
		int posMaxPctg = -1;

		for (int i = 0; i < pctgPorClasse.length; i++) {
			if (pctgPorClasse[i] > maxPctg) {
				maxPctg = pctgPorClasse[i];
				posMaxPctg = i;
			}
		}

		return posMaxPctg;
	}

}