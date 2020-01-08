public class Player {
	// data
	private int _nInHandMoney; // 소지금
	private int _nProperty; // 보유 자산
	private int _nCurrentPosition; // 현재 위치
	private boolean[] _groundList; // 소유한 땅 정보
	private boolean[][] _buildingList; // 소유한 건물 정보
	private boolean _bIsBankruptcy;// 파산여부 저장
	private int _accumulateDice;// 한 바퀴 돌 동안의 주사위 누적 합

	// method
	// constructor
	public Player() {
		_nInHandMoney = 1000; // 초기 자금 미정
		_nProperty = 0; // 초기 자산
		_nCurrentPosition = 12; // 시작 위치 12, start의 index가 12부터 시작함
		_groundList = new boolean[16];
		_buildingList = new boolean[16][3];
		for (int i = 0; i < 16; i++) {
			_groundList[i] = false;
			for (int j = 0; j < 3; j++) {
				_buildingList[i][j] = false; // 각 땅의 3종류의 건물이 비어있음을 표시
			}
		}
		_bIsBankruptcy = false; // 파산 x
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

	public void setInHandMoneyPlusSalary(int money) {// 월급/보너스 받는 set 추가
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

	public void setAccumulateDice(int num) {// 한 판에서의 누적 주사위 값 갱신
		_accumulateDice = _accumulateDice + num;
	}

	public void setAccumulateDiceZero() {// 시작 위치로 갔을때
		_accumulateDice = 0;
	}

	public int getAccumulateDice() {// 한 판에서 누적 주사위 값 반환
		return _accumulateDice;
	}

	// functional

	// 구입할 땅의 index와, 땅의 가격
	public boolean PurchaseGround(int groundIndex, int price) {
		if (_nInHandMoney >= price) {
			_nInHandMoney -= price;
			_nProperty += price;
			_groundList[groundIndex] = true;
			return true;
		}
		return false;
	}

	// 건물을 구입할 땅 index와, 건물의 index(종류), 건물의 가격
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

	// 처분할 땅 index, 땅의 가격
	public boolean groundDisposal(int groundIndex, int price) {
		if (!_groundList[groundIndex]) {// 건물이 없다면
			return false;
		}
		_groundList[groundIndex] = false;// 땅 처분
		_nInHandMoney += price;
		_nProperty -= price;
		return true;
	}

	// 처분할 건물이 있는 땅 index와, 건물의 index, 건물의 가격
	public boolean buildingDisposal(int groundIndex, int buildingIndex, int price) {
		if (!_groundList[groundIndex]) {
			return false;
		}
		if (_buildingList[groundIndex][buildingIndex] == false) {
			return false;
		}
		_buildingList[groundIndex][buildingIndex] = false;// 건물 처분
		_nInHandMoney += price;
		_nProperty -= price;
		return true;
	}

	public void initPosition(int num) {
		_nCurrentPosition = num;
	}
}
