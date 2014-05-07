package SQLite.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

class PillReminderBDAdapter {
	// Campos de la BD
	public static final String CAMPO_ID = "_id";
	public static final String CAMPO_NOMBRE = "nombre";
	public static final String CAMPO_ORIGEN = "origen";
	public static final String CAMPO_DESCRIPCION = "descripcion";
	private static final String TABLA_BD = "nombres";
	private Context contexto;
	private SQLiteDatabase basedatos;
	private PillReminderDBHelper bdHelper;
	// constructor
	public PillReminderBDAdapter(Context context) {
		this.contexto = context;
	}
	// M�todo que abre la BD
	public PillReminderBDAdapter abrir() throws SQLException {
		// Abrimos la base de datos en modo escritura
		bdHelper = new PillReminderDBHelper(contexto);
		basedatos = bdHelper.getReadableDatabase();
		return this;
	}
	// M�todo que cierra la BD
		public void cerrar() {
		bdHelper.close();
	}

		// Devuelve un Cursor con la consulta a todos los registros de la BD
	public Cursor obtenerNombres() {
		return basedatos.query(TABLA_BD, new String[] {CAMPO_NOMBRE },
		null, null, null, null, null);
	}
	// Devuelve la Nota del id
	public Cursor getNombre(String nombre) throws SQLException {
		Cursor mCursor = basedatos.query(true, TABLA_BD, new String[] {
		CAMPO_ID, CAMPO_NOMBRE, CAMPO_ORIGEN, CAMPO_DESCRIPCION },
		CAMPO_NOMBRE + "=?", new String[] {nombre }, null, null, null,null);
		// Nos movemos al primer registro de la consulta
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
	return mCursor;
	}
}