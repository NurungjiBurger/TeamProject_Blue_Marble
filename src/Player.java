public class Player {
	// data
	private int _nInHandMoney; // ������
	private int _nProperty; // ���� �ڻ�
	private int _nCurrentPosition; // ���� ��ġ
	private boolean[] _groundList; // ������ �� ����
	private boolean[][] _buildingList; // ������ �ǹ� ����
	private boolean _bIsBankruptcy;// �Ļ꿩�� ����
	private int _accumulateDice;// �� ���� �� ������ �ֻ��� ���� ��

	// method
	// constructor
	public Player() {
		_nInHandMoney = 1000; // �ʱ� �ڱ� ����
		_nProperty = 0; // �ʱ� �ڻ�
		_nCurrentPosition = 12; // ���� ��ġ 12, start�� index�� 12���� ������
		_groundList = new boolean[16];
		_buildingList = new boolean[16][3];
		for (int i = 0; i < 16; i++) {
			_groundList[i] = false;
			for (int j = 0; j < 3; j++) {
				_buildingList[i][j] = false; // �� ���� 3������ �ǹ��� ��������� ǥ��
			}
		}
		_bIsBankruptcy = false; // �Ļ� x
		_accumulateDice = 0;
	}

	// get
	public int getAllMoney() {
		return _nInHandMoney + _nProperty;
	}

	public int getInHandMoney() {
		return _nInHandMoney;
	}

	public int getProperty() {
		return _nProperty;
	}

	public int getCurrentPosition() {
		return _nCurrentPosition;
	}

	// set
	public void setInHandMoney(int money) {
		_nInHandMoney = money;
	}

	public void setInHandMoneyPlusSalary(int money) {// ����/���ʽ� �޴� set �߰�
		_nInHandMoney += money;
	}

	public void setProperty(int property) {
		_nProperty = property;
	}

	public void setCurrentPosition(int diceValue) {
		_nCurrentPosition = (_nCurrentPosition + diceValue) % 16;
	}

	public void setInHandMoneyMinus(int money) {
		_nInHandMoney -= money;
	}
	
	public void setInHandMoneyPlus(int money) {
		_nInHandMoney += money;
	}

	public boolean getIsBankruptcy() {
		return _bIsBankruptcy;
	}

	public void setIsBankruptcy(boolean b) {
		_bIsBankruptcy = b;
	}

	public void setAccumulateDice(int num) {// �� �ǿ����� ���� �ֻ��� �� ����
		_accumulateDice = _accumulateDice + num;
	}

	public void setAccumulateDiceZero() {// ���� ��ġ�� ������
		_accumulateDice = 0;
	}

	public int getAccumulateDice() {// �� �ǿ��� ���� �ֻ��� �� ��ȯ
		return _accumulateDice;
	}

	// functional

	// ������ ���� index��, ���� ����
	public boolean PurchaseGround(int groundIndex, int price) {
		if (_nInHandMoney >= price) {
			_nInHandMoney -= price;
			_nProperty += price;
			_groundList[groundIndex] = true;
			return true;
		}
		return false;
	}

	// �ǹ��� ������ �� index��, �ǹ��� index(����), �ǹ��� ����
	public boolean PurchaseBuilding(int groundIndex, int buildingIndex, int price) {
		if (!_groundList[groundIndex]) {
			return false;
		}
		if (_nInHandMoney >= price) {
			_nInHandMoney -= price;
			_nProperty += price;
			_buildingList[groundIndex][buildingIndex] = true;
			return true;
		}
		return false;
	}

	// ó���� �� index, ���� ����
	public boolean groundDisposal(int groundIndex, int price) {
		if (!_groundList[groundIndex]) {// �ǹ��� ���ٸ�
			return false;
		}
		_groundList[groundIndex] = false;// �� ó��
		_nInHandMoney += price;
		_nProperty -= price;
		return true;
	}

	// ó���� �ǹ��� �ִ� �� index��, �ǹ��� index, �ǹ��� ����
	public boolean buildingDisposal(int groundIndex, int buildingIndex, int price) {
		if (!_groundList[groundIndex]) {
			return false;
		}
		if (_buildingList[groundIndex][buildingIndex] == false) {
			return false;
		}
		_buildingList[groundIndex][buildingIndex] = false;// �ǹ� ó��
		_nInHandMoney += price;
		_nProperty -= price;
		return true;
	}

	public void initPosition(int num) {
		_nCurrentPosition = num;
	}
}
