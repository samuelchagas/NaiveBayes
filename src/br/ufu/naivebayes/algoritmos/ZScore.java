package br.ufu.naivebayes.algoritmos;

import java.util.List;
import java.util.Map;

import br.ufu.naivebayes.modelos.Amostra;
import br.ufu.naivebayes.modelos.Classe;

public class ZScore {

	public static void aplicar(NaiveBayes naiveBayes) {
		// calcula as medias por atributo de todas as amostras
		double mediasGeral[] = getMediasGeral(naiveBayes.getAmostrasGeral(), naiveBayes.getN(), naiveBayes.getD());

		// calcula os desvios padrao por atributo de todas as amostras
		double desviosGeral[] = getDesviosPadraoGeral(naiveBayes.getAmostrasGeral(), mediasGeral, naiveBayes.getN(), naiveBayes.getD());

		// algoritmo zscore
		for (Map.Entry<Classe, List<Amostra>> entry : naiveBayes.getAmostrasGeral().entrySet()) {
			for (Amostra amostra : entry.getValue()) {
				for (int i = 0; i < naiveBayes.getD(); i++) {
					amostra.getAtributos()[i] = (amostra.getAtributos()[i] - mediasGeral[i]) / desviosGeral[i];
				}
			}
		}
	}

	private static double[] getMediasGeral(Map<Classe, List<Amostra>> amostrasGeral, int n, int d) {
		double mediasGeral[] = new double[d];

		for (Map.Entry<Classe, List<Amostra>> entry : amostrasGeral.entrySet()) {
			for (Amostra amostra : entry.getValue()) {
				for (int i = 0; i < d; i++) {
					mediasGeral[i] += amostra.getAtributos()[i];
				}
			}
		}
		for (int i = 0; i < d; i++) {
			mediasGeral[i] = mediasGeral[i] / n;
		}

		return mediasGeral;
	}

	private static double[] getDesviosPadraoGeral(Map<Classe, List<Amostra>> amostrasGeral, double mediasGeral[], int n, int d) {
		double dp[] = new double[d];

		for (Map.Entry<Classe, List<Amostra>> entry : amostrasGeral.entrySet()) {
			for (Amostra amostra : entry.getValue()) {
				for (int i = 0; i < d; i++) {
					dp[i] += Math.pow((amostra.getAtributos()[i] - mediasGeral[i]), 2);
				}
			}
		}
		for (int i = 0; i < d; i++) {
			dp[i] = Math.sqrt(dp[i] / (n - 1)); // variancia amostral (n - 1)
		}

		return dp;
	}

}
