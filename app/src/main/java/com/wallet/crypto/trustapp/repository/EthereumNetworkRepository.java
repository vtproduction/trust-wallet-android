package com.wallet.crypto.trustapp.repository;

import android.text.TextUtils;

import com.wallet.crypto.trustapp.entity.NetworkInfo;
import com.wallet.crypto.trustapp.entity.Ticker;
import com.wallet.crypto.trustapp.service.TickerService;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Single;

import static com.wallet.crypto.trustapp.C.CLASSIC_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.ETC_SYMBOL;
import static com.wallet.crypto.trustapp.C.ETHEREUM_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.ETH_SYMBOL;
import static com.wallet.crypto.trustapp.C.KOVAN_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.POA_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.POA_SYMBOL;
import static com.wallet.crypto.trustapp.C.ROPSTEN_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.SOKOL_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.TMC_NETWORK_NAME;
import static com.wallet.crypto.trustapp.C.TMC_SYMBOL;

public class EthereumNetworkRepository implements EthereumNetworkRepositoryType {

	private final NetworkInfo[] NETWORKS = new NetworkInfo[] {
			new NetworkInfo(ETHEREUM_NETWORK_NAME, ETH_SYMBOL,
                    "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://api.trustwalletapp.com/",
                    "https://etherscan.io/tx/",1, true),
            new NetworkInfo(CLASSIC_NETWORK_NAME, ETC_SYMBOL,
                    "https://mewapi.epool.io/",
                    "https://classic.trustwalletapp.com",
                    "https://gastracker.io/tx/",61, true),
			new NetworkInfo(POA_NETWORK_NAME, POA_SYMBOL,
					"https://core.poa.network/",
					"https://poa.trustwalletapp.com",
					"https://poaexplorer.com/txid/search/", 99, false),
			new NetworkInfo(TMC_NETWORK_NAME, TMC_SYMBOL,
					"https://core.tomocoin.io/",
					"https://tomo-trust-wallet.herokuapp.com/",
					"https://explorer.tomocoin.io", 40686, true),
			new NetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
                    "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://kovan.trustwalletapp.com/",
                    "https://kovan.etherscan.io/tx/", 42, false),
			new NetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
                    "https://ropsten.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://ropsten.trustwalletapp.com/",
                    "https://ropsten.etherscan.io/tx/",3, false),
            new NetworkInfo(SOKOL_NETWORK_NAME, POA_SYMBOL,
                    "https://sokol.poa.network",
                    "https://trust-sokol.herokuapp.com/",
                    "https://sokol-explorer.poa.network/account/",77, false),
	};

	private final PreferenceRepositoryType preferences;
    private final TickerService tickerService;
    private NetworkInfo defaultNetwork;
    private final Set<OnNetworkChangeListener> onNetworkChangedListeners = new HashSet<>();

    public EthereumNetworkRepository(PreferenceRepositoryType preferenceRepository, TickerService tickerService) {
		this.preferences = preferenceRepository;
		this.tickerService = tickerService;
		defaultNetwork = getByName(preferences.getDefaultNetwork());
		if (defaultNetwork == null) {
			defaultNetwork = NETWORKS[0];
		}
	}

	private NetworkInfo getByName(String name) {
		if (!TextUtils.isEmpty(name)) {
			for (NetworkInfo NETWORK : NETWORKS) {
				if (name.equals(NETWORK.name)) {
					return NETWORK;
				}
			}
		}
		return null;
	}

	@Override
	public NetworkInfo getDefaultNetwork() {
		return defaultNetwork;
	}

	@Override
	public void setDefaultNetworkInfo(NetworkInfo networkInfo) {
		defaultNetwork = networkInfo;
		preferences.setDefaultNetwork(defaultNetwork.name);

		for (OnNetworkChangeListener listener : onNetworkChangedListeners) {
		    listener.onNetworkChanged(networkInfo);
        }
	}

	@Override
	public NetworkInfo[] getAvailableNetworkList() {
		return NETWORKS;
	}

	@Override
	public void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged) {
        onNetworkChangedListeners.add(onNetworkChanged);
	}

    @Override
    public Single<Ticker> getTicker() {
        return Single.fromObservable(tickerService
                .fetchTickerPrice(getDefaultNetwork().symbol));
    }
}
