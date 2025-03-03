package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Eine Dokumentklasse, die die maximale Anzahl an Zeichen für ein JTextField
 * begrenzt.
 */
class LimitedDocument extends PlainDocument {
	private int maxChars;

	/**
	 * Erstellt ein neues LimitedDocument mit einer Zeichenbegrenzung.
	 *
	 * @param maxChars Die maximale Anzahl an Zeichen, die eingegeben werden dürfen.
	 */
	public LimitedDocument(int maxChars) {
		this.maxChars = maxChars;
	}

	/**
	 * Fügt einen String in das Dokument ein, falls das Zeichenlimit nicht
	 * überschritten wird.
	 *
	 * @param offset Die Position, an der der Text eingefügt werden soll.
	 * @param str    Der einzufügende String.
	 * @param attr   Die Attribute des eingefügten Textes.
	 * @throws BadLocationException Falls die Einfügeposition ungültig ist.
	 */
	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		if ((getLength() + str.length()) < maxChars) {
			super.insertString(offset, str, attr);
		}
	}
}
