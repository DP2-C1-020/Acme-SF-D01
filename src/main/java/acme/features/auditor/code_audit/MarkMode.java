
package acme.features.auditor.code_audit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import acme.entities.audits.Mark;

public class MarkMode {

	/**
	 * Encuentra la moda (el valor que aparece con mayor frecuencia) en una colección de marks.
	 * 
	 * @param marks
	 * 
	 * @return La moda como una cadena de texto.
	 */
	public static String findMode(final Collection<Mark> marks) {
		if (marks == null || marks.isEmpty())
			return null;

		Map<Mark, Integer> markFrequencyMap = new HashMap<>();

		// Iterar sobre las marcas y contar su frecuencia
		for (Mark mark : marks)
			markFrequencyMap.put(mark, markFrequencyMap.getOrDefault(mark, 0) + 1);

		// Variables para almacenar la moda y su frecuencia máxima
		Mark mode = null;
		int maxFrequency = 0;

		// Encontrar la moda y su frecuencia máxima
		for (Map.Entry<Mark, Integer> entry : markFrequencyMap.entrySet()) {
			Mark currentMark = entry.getKey();
			int currentFrequency = entry.getValue();

			// Si la frecuencia actual es mayor que la máxima encontrada hasta ahora
			// o si es igual pero la marca es "mayor" (en términos de ordinal)
			if (currentFrequency > maxFrequency || currentFrequency == maxFrequency && currentMark.ordinal() > mode.ordinal()) {
				mode = currentMark;
				maxFrequency = currentFrequency;
			}
		}

		return mode.toString();
	}

}
