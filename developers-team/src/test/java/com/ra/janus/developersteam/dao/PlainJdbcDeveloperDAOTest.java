package com.ra.janus.developersteam.dao;

public class PlainJdbcDeveloperDAOTest {
    private static final String SELECT_ALL_SQL = "SELECT * FROM DEVELOPER";
    private static final String UPDATE_SQL = "UPDATE DEVELOPER SET NAME = ? WHERE ID = ?";
    private static final String DELETE_SQL = "DELETE FROM DEVELOPER WHERE ID = ?";
    private static final String SELECT_ONE_SQL = "SELECT * FROM DEVELOPER WHERE ID = ?";
    private static final String INSERT_SQL = "INSERT INTO DEVELOPER (ID, NAME) VALUES (?, ?)";

    private DataSource mockDataSource;

    private PlainJdbcDeveloperDAO DeveloperDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        DeveloperDAO = new PlainJdbcDeveloperDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateDeveloperShouldReturnDeveloper() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Developer testDeveloper = new Developer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Developer Developer = DeveloperDAO.create(testDeveloper);

        //then
        assertEquals(testDeveloper, Developer);
    }

    @Test
    void whenCreateDeveloperShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Developer testDeveloper = new Developer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> DeveloperDAO.create(testDeveloper);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateDeveloperShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Developer testDeveloper = new Developer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.create(testDeveloper);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadDeveloperFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Developer Developer = DeveloperDAO.read(testId);

        //then
        assertEquals(testId, Developer.getId());
    }

    @Test
    void whenReadAbsentDeveloperFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Developer expectedDeveloper = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Developer Developer = DeveloperDAO.read(testId);

        //then
        assertEquals(expectedDeveloper, Developer);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        final int testParametherIndex = 1;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadAllDevelopersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Developer> list = DeveloperDAO.readAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllDevelopersFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.readAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateDeveloperInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Developer testDeveloper = new Developer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = DeveloperDAO.update(testDeveloper);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateDeveloperInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Developer testDeveloper = new Developer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = DeveloperDAO.update(testDeveloper);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateDeveloperInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Developer testDeveloper = new Developer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.update(testDeveloper);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteDeveloperFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = DeveloperDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteDeveloperFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = DeveloperDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteDeveloperFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> DeveloperDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}
