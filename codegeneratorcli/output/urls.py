
from .views import agent_views, item_views, item_collaborator_views
from django.urls import re_path, path

urlpatterns = [
    re_path(r'api/agents/(?P<agent_id>\d+)/', agent_views.handle_agent),

    re_path(r'api/items/(?P<item_id>[0-9a-zA-Z-]+)/', item_views.handle_item),

    path('api/agents/', agent_views.handle_agents),
    path('api/items/', item_views.handle_items),
    re_path(r'api/items/for-agent/(?P<agent_id>\d+)/', item_views.get_items_for_agent),
    re_path(r'api/agents/(?P<username>\w+)/', agent_views.get_agent_by_username),

    re_path(r'api/items/(?P<title>\w+)/', item_views.get_item_by_title),

    path('api/itemcollaborators/', item_collaborator_views.create_item_collaborator),

    re_path(r'api/itemcollaborators/(?P<item>[0-9a-zA-Z-]+)/(?P<collaborator_id>\d+)/', item_collaborator_views.delete_item_collaborator),

    re_path(r'api/itemcollaborators/(?P<item>[0-9a-zA-Z-]+)/', item_collaborator_views.get_item_collaborators_for_item),
]
