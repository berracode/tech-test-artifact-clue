package co.com.bancolombia.galatea.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import co.com.bancolombia.galatea.Matrix;
import co.com.bancolombia.galatea.dto.ManuscriptRequest;
import co.com.bancolombia.galatea.dto.StatsResponse;
import co.com.bancolombia.galatea.entity.ManuscriptEntity;
import co.com.bancolombia.galatea.repository.ManuscriptRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtifactService {
    private final ManuscriptRepository manuscriptRepository;

    private final Map<String, Boolean> cache = new ConcurrentHashMap<>();
    private final AtomicInteger countClue = new AtomicInteger(0);
    private final AtomicInteger countNoClue = new AtomicInteger(0);

    public boolean analyzeManuscript(ManuscriptRequest manuscript) {
        String hash = hashManuscript(manuscript.getManuscript());

        Boolean cached = cache.get(hash);
        if (cached != null) {
            return cached;
        }

        Optional<ManuscriptEntity> existing = manuscriptRepository.findByHash(hash);
        if (existing.isPresent()) {
            boolean hasClue = existing.get().isHasClue();
            cache.put(hash, hasClue);
            return hasClue;
        }

        boolean hasClue =
                containsArtifactClue(manuscript.getManuscript().toArray(new String[0]));

        cache.put(hash, hasClue);

        if (hasClue) {
            countClue.incrementAndGet();
        } else {
            countNoClue.incrementAndGet();
        }

        ManuscriptEntity entity =
                ManuscriptEntity.builder().hash(hash).hasClue(hasClue).build();
        manuscriptRepository.save(entity);
        return hasClue;
    }

    public StatsResponse getStats() {
        int clue = countClue.get();
        int noClue = countNoClue.get();
        double ratio = noClue == 0 ? 0 : (double) clue / (double) noClue;

        return new StatsResponse(clue, noClue, ratio);
    }

    private String formatManuscript(List<String> lines) {
        return String.join("##", lines);
    }


    private String hashManuscript(List<String> manuscript) {
        String text = String.join("\n", manuscript);
        return DigestUtils.sha256Hex(text);
    }

    private Matrix buildMatrix(String[] manuscript) {
        //todo: encontrar la linea más larga para obtener las columnas
        int rows = manuscript.length;
        String largestLine = Arrays.stream(manuscript).max(Comparator.comparing(String::length)).orElse(null);
        System.out.println("Largest line: " + largestLine);
        if (largestLine == null) {
            throw new RuntimeException("No se encontró la línea más larga");
        }
        int cols = largestLine.length();

        var matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            var fullRow = manuscript[i];

            for (int j = 0; j < fullRow.length(); j++) {
                var item = fullRow.substring(j, j + 1);
                matrix.set(item, i, j);
            }
        }
        var text = String.format("Matrix [%s] [%s]", rows, cols);
        System.out.println(text);
        matrix.print();

        return matrix;

    }

    private boolean containsArtifactClue(String[] manuscript) {

        //TODO: generar matrices con lineas del mismo tamaño
        var matrix = buildMatrix(manuscript);

        int availableRowsBelow = 0;
        int availableColsRight = 0;
        int availableColsLeft = 0;
        int requiredSpace = 3;

        var data = matrix.getData();
        int rows = matrix.getRows();
        int cols = matrix.getCols();
        int clue = 0;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {
                availableRowsBelow = (rows - 1) - i;
                availableColsRight = (cols - 1) - j;// (6 -1 ) -J = 5-J = 5-3 = 2
                availableColsLeft = j;

                var item = data[i][j];


                //BUSQUEDA HORIZONTAL DER
                if (availableColsRight >= requiredSpace) {
                    int indexCol = j;//0,
                    int totalIterations = 0;//0,
                    do {
                        var nextItem = data[i][indexCol + 1];
                        if (null == nextItem || !nextItem.equalsIgnoreCase(item)) {
                            break;
                        }
                        indexCol++;//1,2
                        totalIterations++;//1,2
                    } while (totalIterations < 3);//1<3, 2<3

                    if (totalIterations == 3) {
                        var text = String.format("Pista HOR encontrada iniciando en [%s][%s]", i, j);
                        System.out.println(text);
                        clue++;
                    }
                }

                //BUSQUEDA DIAGONAL DER
                if (availableColsRight >= requiredSpace && availableRowsBelow >= requiredSpace) {
                    int indexCol = j;//3,
                    int indexRow = i;//0,
                    int totalIterations = 0;//0,

                    do {
                        var nextItem = data[indexRow + 1][indexCol + 1];//1,3 - 2,4 - 3,5
                        if (null == nextItem || !nextItem.equalsIgnoreCase(item)) {
                            break;
                        }
                        indexCol++;//3,4
                        indexRow++;//1,2
                        totalIterations++;//1,2
                    } while (totalIterations < 3);//1<3,2<3

                    if (totalIterations == 3) {
                        var text = String.format("Pista DIAG DER encontrada iniciando en [%s][%s]", i, j);
                        System.out.println(text);
                        clue++;
                    }
                }

                //BUSQUEDA VERTICAL DER
                if (availableRowsBelow >= requiredSpace) {
                    int indexRow = i;//1,
                    int totalIterations = 0;//0,
                    do {
                        var nextItem = data[indexRow + 1][j];// 2,0 - 3,0 - 4,0
                        if (null == nextItem || !nextItem.equalsIgnoreCase(item)) {
                            break;
                        }
                        indexRow++;//2,3,4
                        totalIterations++;//1, 2, 3
                    } while (totalIterations < 3);//1<3, 2<3, 3<3-

                    if (totalIterations == 3) {
                        var text = String.format("Pista VER encontrada iniciando en [%s][%s]", i, j);
                        System.out.println(text);
                        clue++;
                    }

                }

                //BUSQUEDA DIAGONAL IZQ
                if (availableColsLeft >= requiredSpace && availableRowsBelow >= requiredSpace) {
                    int indexCol = j;//3,
                    int indexRow = i;//0,
                    int totalIterations = 0;//0,

                    do {
                        var nextItem = data[indexRow + 1][indexCol - 1];// 1,2 - 2,1 - 3,0
                        if (null == nextItem || !nextItem.equalsIgnoreCase(item)) {
                            break;
                        }
                        indexCol--;//2,1,0
                        indexRow++;//1,2, 3
                        totalIterations++;//1,2, 3
                    } while (totalIterations < 3);//1<3, 2<3, 3<3

                    if (totalIterations == 3) {
                        var text = String.format("Pista DIAG IZQ encontrada iniciando en [%s][%s]", i, j);
                        System.out.println(text);
                        clue++;
                    }
                }


            }

        }

        System.out.println("pistas: " + clue);

        return clue > 0;
    }
}
