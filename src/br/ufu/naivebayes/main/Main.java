package br.ufu.naivebayes.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import br.ufu.naivebayes.algoritmos.NaiveBayes;
import br.ufu.naivebayes.algoritmos.ZScore;
import br.ufu.naivebayes.modelos.Amostra;
import br.ufu.naivebayes.modelos.Classe;

public class Main {

	public static void main(String[] args) {

		if (args.length == 4) {

			String filePath = args[0];

			try {

				int nroAcertos = 0;
				String respostaNaiveBayes = null;

				int nroExecucoes = Integer.valueOf(args[1]);
				double pctgHoldOut = Double.valueOf(args[2].split("/")[0]) / Double.valueOf(args[2].split("/")[1]);
				boolean aplicaZScore = args[3].equalsIgnoreCase("s");

				NaiveBayes naiveBayes = getInstanciaNaiveBayes(filePath);

				// aplica zscore
				if (aplicaZScore) {
					ZScore.aplicar(naiveBayes);
				}

				for (int exe = 0; exe < nroExecucoes; exe++) {

					// embaralha as amostras
					for (Map.Entry<Classe, List<Amostra>> entry : naiveBayes.getAmostrasGeral().entrySet()) {
						Collections.shuffle(entry.getValue());
					}

					// monta lista com as amostras de treinamento
					naiveBayes.setAmostrasTreinamento(new ArrayList<Amostra>());
					for (Map.Entry<Classe, List<Amostra>> entry : naiveBayes.getAmostrasGeral().entrySet()) {
						for (int i = 0; i < Math.round(entry.getValue().size() * pctgHoldOut); i++) {
							naiveBayes.getAmostrasTreinamento().add(entry.getValue().get(i));
						}
					}

					// monta lista com as amostras de validacao
					naiveBayes.setAmostrasValidacao(new ArrayList<Amostra>());
					for (Map.Entry<Classe, List<Amostra>> entry : naiveBayes.getAmostrasGeral().entrySet()) {
						for (int i = (int) Math.round(entry.getValue().size() * pctgHoldOut); i < entry.getValue().size(); i++) {
							naiveBayes.getAmostrasValidacao().add(entry.getValue().get(i));
						}
					}

					// treina o algoritmo naive bayes
					naiveBayes.treinar();

					// calcula porcentagem de acertos
					for (Amostra amostraValidacao : naiveBayes.getAmostrasValidacao()) {

						// classifica amostra utilizando naive bayes
						respostaNaiveBayes = naiveBayes.classificar(amostraValidacao);

						if (respostaNaiveBayes.equals(amostraValidacao.getClasse())) {
							nroAcertos++;
						}
					}

				}

				int qtdeValidacao = naiveBayes.getAmostrasValidacao().size() * nroExecucoes;
				System.out.println(String.format("Numero de execucoes: %d.", nroExecucoes));
				System.out.println(String.format("  Numero de acertos: %d. -- %.2f%% --", nroAcertos, (nroAcertos * 100.0 / (qtdeValidacao))));
				System.out.println(String.format("    Numero de erros: %d. -- %.2f%% --", (qtdeValidacao - nroAcertos),
						((qtdeValidacao - nroAcertos) * 100.0 / qtdeValidacao)));

			} catch (FileNotFoundException e) {
				System.err.println("Arquivo '" + filePath + "' nao encontrado!");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			System.err.println("Comando incorreto. Para rodar o programa, digite os argumentos na seguinte ordem:\n"
					+ "'Caminho completo do arquivo' 'Quantidade de execucoes' 'Fracao Hold Out' 'Aplicar zscore (s/n)'\n"
					+ "Exemplo: java -jar NaiveBayes.jar /tmp/arquivo.txt 10 2/3 n");

		}

	}

	private static NaiveBayes getInstanciaNaiveBayes(String filePath) throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader(filePath));
		sc.useLocale(new Locale("en", "US"));

		NaiveBayes naiveBayes = new NaiveBayes();
		Amostra amostra = null;
		Classe classe = null;
		List<Amostra> newList;

		naiveBayes.setN(sc.nextInt());
		naiveBayes.setD(sc.nextInt());
		sc.nextLine();

		naiveBayes.setAmostrasGeral(new HashMap<Classe, List<Amostra>>());
		for (int i = 0; i < naiveBayes.getN(); i++) {
			amostra = new Amostra();
			amostra.setAtributos(new double[naiveBayes.getD()]);
			for (int j = 0; j < naiveBayes.getD(); j++) {
				amostra.getAtributos()[j] = sc.nextDouble();
			}

			classe = new Classe(sc.next());
			amostra.setClasse(classe.getId());
			if (naiveBayes.getAmostrasGeral().containsKey(classe)) {
				newList = new ArrayList<Amostra>(naiveBayes.getAmostrasGeral().get(classe));
				newList.add(amostra);
			} else {
				newList = Arrays.asList(amostra);
			}

			naiveBayes.getAmostrasGeral().put(classe, newList);

		}
		sc.close();

		return naiveBayes;
	}
}
