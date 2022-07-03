package com.github.kerminal.storage.adapter;

import com.github.kerminal.models.Home;
import com.github.kerminal.utils.LocationAdapter;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import org.bukkit.Location;
public class HomeAdapter implements SQLResultAdapter<Home> {
    @Override
    public Home adaptResult(SimpleResultSet rs) {
        final String name = rs.get("name");
        final int isDefault = rs.get("is_default");
        final Location location = LocationAdapter.toLocation(rs.get("location"));

        return new Home(name, location, isDefault == 0);
    }
}
