package com.github.larryr1.verilock.data.importing;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.data.PlayerIdentity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IdentityImporter {
    public static IdentityImportResult readData() throws IOException {

        // Immediately start timer
        final long startTime = System.currentTimeMillis();

        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        String dataFolderPath = Verilock.getInstance().getDataFolder().getAbsolutePath();
        File csvFile = new File(Paths.get(dataFolderPath, "playerIdentityImport.csv").toString());



        CsvSchema schema = mapper.schemaFor(PlayerIdentity.class).withHeader(); // schema from PlayerIdentity definition
        MappingIterator<PlayerIdentity> it = mapper.readerFor(PlayerIdentity.class).with(schema)
                .readValues(csvFile);

        List<PlayerIdentity> all = it.readAll();

        AtomicInteger countIdentitiesSkipped = new AtomicInteger(0);
        AtomicInteger countIdentitiesAdded = new AtomicInteger(0);

        all.forEach(identity -> {

            if (Verilock.getInstance().verificationDatabase.IdentityExistsById(identity.getIdNumber())) {
                countIdentitiesSkipped.incrementAndGet();
                return;
            }

            // Attempt the database insert
            try {
                Verilock.getInstance().verificationDatabase.CreateIdentity(identity);
                countIdentitiesAdded.incrementAndGet();
            } catch (SQLException e) {
                Verilock.getInstance().logger.warning(e.toString());
            }
        });

        Verilock.getInstance().logger.info("Added " + countIdentitiesAdded.get() + " identities to the database.");
        if (countIdentitiesSkipped.get() > 0) {
            Verilock.getInstance().logger.info("Skipped " + countIdentitiesSkipped + " identities because they already existed in the database.");
        }


        return new IdentityImportResult(countIdentitiesAdded.get(), countIdentitiesSkipped.get(), System.currentTimeMillis() - startTime);
    }
}
