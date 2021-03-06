package seedu.duke.api;

import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;
import seedu.duke.exception.DukeException;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockPriceFetcher {
    private static Logger logger = Logger.getLogger("tp");
    private String[] apiKeys = {"O8EVQ7YSWDW08BN9", "3FPNCQ0JNYEE8BTK"};
    private int timeout = 3000;

    public StockPriceFetcher() {
        timeout = 3000;
    }

    public double fetchLatestPrice(String symbol) throws DukeException {
        logger.log(Level.INFO, "fetching latest stock data for " + symbol);
        StockData stockData = fetchLatestStockData(symbol);

        return (stockData.getHigh() + stockData.getLow()) / 2;
    }

    public StockData fetchLatestStockData(String symbol) throws DukeException {
        Random random = new Random();
        String randomApiKey = apiKeys[random.nextInt(apiKeys.length)];
        AlphaVantageConnector apiConnector = new AlphaVantageConnector(randomApiKey, timeout);
        TimeSeries stockTimeSeries = new TimeSeries(apiConnector);

        try {
            IntraDay response = stockTimeSeries.intraDay(symbol, Interval.ONE_MIN, OutputSize.COMPACT);
            List<StockData> stockData = response.getStockData();

            return stockData.get(0);
        } catch (AlphaVantageException e) {
            logger.log(Level.INFO, "failed to fetch price from API");
            throw new DukeException("Failed to retrieve price! Please try again.");
        }
    }

}
