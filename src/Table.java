public class Table {
    private static final int PAGE_SIZE = 4096;
    private static final int TABLE_MAX_PAGES = 100;
    private static final int ROW_SIZE = Row.ROW_SIZE;

    private static final int ROWS_PER_PAGE = PAGE_SIZE / ROW_SIZE;

    private static final int TABLE_MAX_ROWS = ROWS_PER_PAGE * TABLE_MAX_PAGES;


    private int numRows;
    private byte[][] pages;

    public Table() {
        this.numRows = 0;
        this.pages = new byte[TABLE_MAX_PAGES][PAGE_SIZE];
    }

    public int getNumRows() {
        return numRows;
    }
    public int getRowsPerPage() {
        return ROWS_PER_PAGE;
    }


    public byte[] getPage(int pageIndex) {
        if (pageIndex >= 0 && pageIndex < TABLE_MAX_PAGES) {
            return pages[pageIndex];
        }
        return null;
    }

    public Row[] getAllRowsFromPage(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= TABLE_MAX_PAGES) {
            System.out.println("Invalid page index.");
            return new Row[0];
        }

        byte[] page = getPage(pageIndex);
        if (page == null) {
            System.out.println("No data available for this page.");
            return new Row[0];
        }

        Row[] rows = new Row[ROWS_PER_PAGE];
        for (int i = 0; i < ROWS_PER_PAGE && i < numRows; i++) {
            int rowStartIndex = i * ROW_SIZE;
            if (rowStartIndex + ROW_SIZE > PAGE_SIZE) {
                break;
            }

            byte[] rowBytes = new byte[ROW_SIZE];
            System.arraycopy(page, rowStartIndex, rowBytes, 0, ROW_SIZE);

            Row row = Row.deserialize(rowBytes);
            if (row != null) {
                rows[i] = row;
            }
        }
        return rows; // Return the array of rows
    }

//    public byte[] getAllPage() {
//        byte[] page = new byte[];
//        if (pages.length != 0) {
//            return pages;
//        }
//        return null;
//    }

    public void addRow(Row row) {
        if (numRows < TABLE_MAX_ROWS) {
            int pageIndex = numRows / ROWS_PER_PAGE;
            int rowIndex = numRows % ROWS_PER_PAGE;
//            System.out.println("ROWS_PER_PAGE: " + ROWS_PER_PAGE);
//            System.out.println("ROW_SIZE: " + ROW_SIZE);
//            System.out.println("numRows: " + numRows);
//            System.out.println("PAGE_SIZE: " + PAGE_SIZE);
//            System.out.println("pageIndex: " + pageIndex);
//            System.out.println("rowIndex: " + rowIndex);
//            System.out.println("TABLE_MAX_ROWS: " + TABLE_MAX_ROWS);

            byte[] serializedRow = row.serialize();
            System.arraycopy(serializedRow, 0, pages[pageIndex], rowIndex * ROW_SIZE, ROW_SIZE);
            numRows++;
        } else {
            System.out.println("Table is full");
        }
    }



}
